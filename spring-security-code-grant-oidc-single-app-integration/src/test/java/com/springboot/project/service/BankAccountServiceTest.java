package com.springboot.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.springboot.project.entity.BankAccountEntity;
import com.springboot.project.generated.model.BankAccountDetailModel;
import com.springboot.project.generated.model.BankAccountFilterRequestModel;
import com.springboot.project.generated.model.BankAccountFilterResponseModel;
import com.springboot.project.generated.model.PaginationRequestModel;
import com.springboot.project.repository.BankAccountRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceTest {

    @Mock private BankAccountRepository bankAccountRepository;

    @InjectMocks private BankAccountService bankAccountService;

    @Test
    void filter_bank_accounts_success() {
        // Given
        BankAccountFilterRequestModel request = new BankAccountFilterRequestModel();
        PaginationRequestModel pagination = new PaginationRequestModel();
        pagination.setPageSize(10);
        pagination.setPageNumber(0);
        request.setPagination(pagination);

        BankAccountEntity entity = new BankAccountEntity();
        entity.setId(java.util.UUID.randomUUID());
        entity.setAccountNumber("123456789");

        Page<BankAccountEntity> page = new PageImpl<>(Collections.singletonList(entity));

        when(bankAccountRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        // When
        BankAccountFilterResponseModel response = bankAccountService.filterBankAccounts(request);

        // Then
        assertNotNull(response);
        assertEquals(1, response.getData().size());
        assertEquals(1, response.getTotalItems());
        assertEquals("123456789", response.getData().get(0).getAccountNumber());
    }

    @Test
    void filter_bank_accounts_empty() {
        // Given
        BankAccountFilterRequestModel request = new BankAccountFilterRequestModel();
        PaginationRequestModel pagination = new PaginationRequestModel();
        pagination.setPageSize(10);
        pagination.setPageNumber(0);
        request.setPagination(pagination);

        Page<BankAccountEntity> page = new PageImpl<>(Collections.emptyList());

        when(bankAccountRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        // When
        BankAccountFilterResponseModel response = bankAccountService.filterBankAccounts(request);

        // Then
        assertNotNull(response);
        assertEquals(0, response.getData().size());
        assertEquals(0, response.getTotalItems());
    }

    @Test
    void filter_bank_accounts_with_cursor_success() {
        // Given
        BankAccountFilterRequestModel request = new BankAccountFilterRequestModel();
        PaginationRequestModel pagination = new PaginationRequestModel();
        pagination.setPageSize(10);
        request.setPagination(pagination);

        BankAccountEntity entity = new BankAccountEntity();
        entity.setId(java.util.UUID.randomUUID());
        entity.setSequenceNumber(100L);

        Page<BankAccountEntity> page = new PageImpl<>(Collections.singletonList(entity));

        when(bankAccountRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);
        
        BankAccountDetailModel detailModel = new BankAccountDetailModel();
        detailModel.setSequenceNumber(100L);

        // When
        BankAccountFilterResponseModel response =
                bankAccountService.filterBankAccountsWithCursor(request);

        // Then
        assertNotNull(response);
        assertEquals(1, response.getData().size());
        assertEquals(1, response.getTotalItems());
        assertEquals(100L, response.getData().get(0).getSequenceNumber());
        assertEquals(100L, response.getNextPageToken());
        assertEquals(100L, response.getPreviousPageToken());
    }
}
