package com.springboot.project.service;

import com.springboot.project.entity.TransactionDetailEntity;
import com.springboot.project.generated.model.TransactionDetailModel;
import com.springboot.project.generated.model.TransactionFilterRequestModel;
import com.springboot.project.generated.model.TransactionFilterResponseModel;
import com.springboot.project.mapper.TransactionDetailMapper;
import com.springboot.project.repository.TransactionRepository;
import com.springboot.project.shared.SpecificationHelper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionFilterResponseModel filterTransactions(
            TransactionFilterRequestModel filterRequest) {
        assert filterRequest.getPagination() != null;
        Pageable pageable = SpecificationHelper.buildPageable(filterRequest.getPagination());
        Example<TransactionDetailEntity> transactionDetailEntityExample =
                this.buildTransactionDetailExample(filterRequest);
        Specification<TransactionDetailEntity> specification =
                SpecificationHelper.init(transactionDetailEntityExample);
        Page<TransactionDetailEntity> pages =
                this.transactionRepository.findAll(specification, pageable);

        List<TransactionDetailModel> data =
                TransactionDetailMapper.MAPPER.toTransactionDetails(pages.toList());
        return new TransactionFilterResponseModel()
                .data(data)
                .foundItems((long) pages.getNumberOfElements())
                .totalItems(pages.getTotalElements());
    }

    public TransactionFilterResponseModel filterTransactionsWithCursor(
            TransactionFilterRequestModel filterRequest) {
        assert filterRequest.getPagination() != null;
        Pageable pageable =
                SpecificationHelper.buildPageableForCursor(filterRequest.getPagination());

        Specification<TransactionDetailEntity> specification =
                SpecificationHelper.init(this.buildTransactionDetailExample(filterRequest));

        Long nextPageToken = filterRequest.getPagination().getNextPageToken();
        Long previousPageToken = filterRequest.getPagination().getPreviousPageToken();

        if (nextPageToken != null) {
            specification =
                    specification.and(
                            SpecificationHelper.cursorPagination(
                                    pageable.getSort(), "sequenceNumber", nextPageToken, false));
        }

        if (previousPageToken != null) {
            specification =
                    specification.and(
                            SpecificationHelper.cursorPagination(
                                    pageable.getSort(), "sequenceNumber", previousPageToken, true));
            pageable =
                    PageRequest.of(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            pageable.getSort().isSorted()
                                    ? pageable.getSort().descending()
                                    : pageable.getSort());
        }

        Page<TransactionDetailEntity> pages =
                transactionRepository.findAll(specification, pageable);

        List<TransactionDetailModel> data =
                TransactionDetailMapper.MAPPER.toTransactionDetails(pages.toList());

        Long nextToken = null;
        Long previousToken = null;
        if (!data.isEmpty()) {
            nextToken = data.get(data.size() - 1).getSequenceNumber();
            previousToken = data.get(0).getSequenceNumber();
        }

        return new TransactionFilterResponseModel()
                .data(data)
                .foundItems((long) data.size())
                .totalItems(pages.getTotalElements())
                .previousPageToken(previousToken)
                .nextPageToken(nextToken);
    }

    private Example<TransactionDetailEntity> buildTransactionDetailExample(
            TransactionFilterRequestModel filterRequestModel) {
        TransactionDetailEntity transactionDetail =
                TransactionDetailMapper.MAPPER.toTransactionDetailEntityFromExample(
                        filterRequestModel);

        ExampleMatcher exampleMatcher =
                ExampleMatcher.matching()
                        .withIgnoreNullValues()
                        .withIgnoreCase()
                        .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        return Example.of(transactionDetail, exampleMatcher);
    }
}
