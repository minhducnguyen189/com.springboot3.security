package com.springboot.project.service;

import com.springboot.project.entity.BankAccountEntity;
import com.springboot.project.generated.model.BankAccountDetailModel;
import com.springboot.project.generated.model.BankAccountFilterRequestModel;
import com.springboot.project.generated.model.BankAccountFilterResponseModel;
import com.springboot.project.mapper.BankAccountMapper;
import com.springboot.project.repository.BankAccountRepository;
import com.springboot.project.shared.SpecificationHelper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public BankAccountFilterResponseModel filterBankAccounts(
            BankAccountFilterRequestModel filterRequest) {
        assert filterRequest.getPagination() != null;
        Pageable pageable = SpecificationHelper.buildPageable(filterRequest.getPagination());
        Example<BankAccountEntity> bankAccountEntityExample =
                this.buildBankAccountExample(filterRequest);
        Specification<BankAccountEntity> specification =
                SpecificationHelper.init(bankAccountEntityExample);
        Page<BankAccountEntity> pages =
                this.bankAccountRepository.findAll(specification, pageable);

        List<BankAccountDetailModel> data =
                BankAccountMapper.MAPPER.toBankAccountDetails(pages.toList());
        return new BankAccountFilterResponseModel()
                .data(data)
                .foundItems((long) pages.getNumberOfElements())
                .totalItems(pages.getTotalElements());
    }

    private Example<BankAccountEntity> buildBankAccountExample(
            BankAccountFilterRequestModel filterRequestModel) {
        BankAccountEntity bankAccount =
                BankAccountMapper.MAPPER.toBankAccountEntityFromExample(filterRequestModel);

        ExampleMatcher exampleMatcher =
                ExampleMatcher.matching()
                        .withIgnoreNullValues()
                        .withIgnoreCase()
                        .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        return Example.of(bankAccount, exampleMatcher);
    }

    public BankAccountFilterResponseModel filterBankAccountsWithCursor(
            BankAccountFilterRequestModel filterRequest) {
        assert filterRequest.getPagination() != null;
        Pageable pageable =
                SpecificationHelper.buildPageableForCursor(filterRequest.getPagination());
        Specification<BankAccountEntity> specification =
                SpecificationHelper.init(this.buildBankAccountExample(filterRequest));

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

        Page<BankAccountEntity> pages =
                bankAccountRepository.findAll(specification, pageable);

        List<BankAccountDetailModel> data =
                BankAccountMapper.MAPPER.toBankAccountDetails(pages.toList());

        Long nextToken = null;
        Long previousToken = null;
        if (!data.isEmpty()) {
            nextToken = data.get(data.size() - 1).getSequenceNumber();
            previousToken = data.get(0).getSequenceNumber();
        }

        return new BankAccountFilterResponseModel()
                .data(data)
                .foundItems((long) data.size())
                .totalItems(pages.getTotalElements())
                .previousPageToken(previousToken)
                .nextPageToken(nextToken);
    }
}
