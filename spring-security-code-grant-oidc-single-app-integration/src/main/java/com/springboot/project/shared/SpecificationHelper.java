package com.springboot.project.shared;

import com.springboot.project.generated.model.PaginationRequestModel;
import jakarta.persistence.criteria.Predicate;
import java.util.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;

public final class SpecificationHelper {

    private SpecificationHelper() {
        throw new IllegalArgumentException("Static class can not be created");
    }

    public static <T> Specification<T> queryDateBetween(
            String checkFieldName, Date dateFrom, Date dateTo) {
        return (root, query, builder) -> {
            if (StringUtils.isEmpty(checkFieldName)) {
                return builder.conjunction();
            }
            List<Predicate> predicates = new ArrayList<>();
            if (Objects.nonNull(dateFrom)) {
                predicates.add(builder.greaterThanOrEqualTo(root.get(checkFieldName), dateFrom));
            }
            if (Objects.nonNull(dateTo)) {
                predicates.add(builder.lessThanOrEqualTo(root.get(checkFieldName), dateTo));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public <T> Specification<T> queryJoinTableNumberEqual(
            String joinTable, String field, Integer value) {
        return (root, query, builder) -> {
            if (Objects.isNull(joinTable) || Objects.isNull(field) || Objects.isNull(value)) {
                return builder.conjunction();
            }
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(joinTable).get(field), value));
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static <T> Specification<T> init(Example<T> example) {
        return (root, query, builder) -> {
            // If example is null or has all null attributes, return all items
            if (Objects.isNull(example) || allAttributesNull(example.getProbe())) {
                return builder.conjunction();
            }
            List<Predicate> predicates = new ArrayList<>();
            // Add predicates based on the example
            predicates.add(QueryByExamplePredicateBuilder.getPredicate(root, builder, example));
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static <T> boolean allAttributesNull(T probe) {
        // Check if all attributes in the probe object are null using reflection
        try {
            Map<String, String> map = BeanUtils.describe(probe);
            for (String value : map.values()) {
                if (value != null) {
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static Pageable buildPageable(PaginationRequestModel paginationRequest) {
        int pageNumber =
                Objects.nonNull(paginationRequest.getPageNumber())
                        ? paginationRequest.getPageNumber()
                        : 0;
        int pageSize =
                Objects.nonNull(paginationRequest.getPageSize())
                        ? paginationRequest.getPageSize()
                        : 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        if (Objects.nonNull(paginationRequest.getSortBy())
                && Objects.nonNull(paginationRequest.getSortOrder())) {
            pageable =
                    PageRequest.of(
                            pageNumber,
                            pageSize,
                            Sort.by(
                                    Sort.Direction.valueOf(
                                            paginationRequest.getSortOrder().getValue()),
                                    paginationRequest.getSortBy()));
        }
        return pageable;
    }

    public static Pageable buildPageableForCursor(PaginationRequestModel paginationRequest) {
        int pageSize =
                paginationRequest.getPageSize() != null ? paginationRequest.getPageSize() : 50;

        Sort sort = Sort.unsorted();
        if (paginationRequest.getSortBy() != null && paginationRequest.getSortOrder() != null) {
            sort =
                    Sort.by(
                            Sort.Direction.valueOf(paginationRequest.getSortOrder().getValue()),
                            paginationRequest.getSortBy());
        }

        return PageRequest.of(0, pageSize, sort);
    }

    public static <T> Specification<T> cursorPagination(
            Sort sort, String idPropertyName, Long cursorValue, boolean isPrevious) {

        return (root, query, cb) -> {
            if (sort == null || StringUtils.isBlank(idPropertyName) || cursorValue == null) {
                return cb.conjunction();
            }

            Sort.Order order = sort.getOrderFor(idPropertyName);
            if (order == null) {
                return cb.conjunction();
            }

            boolean ascending = order.isAscending();

            if (isPrevious) {
                ascending = !ascending;
            }

            return ascending
                    ? cb.greaterThan(root.get(idPropertyName), cursorValue)
                    : cb.lessThan(root.get(idPropertyName), cursorValue);
        };
    }
}
