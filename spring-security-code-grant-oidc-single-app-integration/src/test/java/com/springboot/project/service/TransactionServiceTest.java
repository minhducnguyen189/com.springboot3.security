package com.springboot.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.springboot.project.entity.TransactionDetailEntity;
import com.springboot.project.generated.model.PaginationRequestModel;
import com.springboot.project.generated.model.TransactionFilterRequestModel;
import com.springboot.project.generated.model.TransactionFilterResponseModel;
import com.springboot.project.repository.TransactionRepository;
import java.util.Collections;
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
class TransactionServiceTest {

    @Mock private TransactionRepository transactionRepository;

    @InjectMocks private TransactionService transactionService;

    @Test
    void filter_transactions_success() {
        // Given
        TransactionFilterRequestModel request = new TransactionFilterRequestModel();
        PaginationRequestModel pagination = new PaginationRequestModel();
        pagination.setPageSize(10);
        pagination.setPageNumber(0);
        request.setPagination(pagination);

        TransactionDetailEntity entity = new TransactionDetailEntity();
        entity.setId(java.util.UUID.randomUUID());
        entity.setSequenceNumber(1L);

        Page<TransactionDetailEntity> page = new PageImpl<>(Collections.singletonList(entity));

        when(transactionRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        // When
        TransactionFilterResponseModel response = transactionService.filterTransactions(request);

        // Then
        assertNotNull(response);
        assertEquals(1, response.getData().size());
        assertEquals(1, response.getTotalItems());
        assertEquals(1L, response.getData().get(0).getSequenceNumber());
    }

    @Test
    void filter_transactions_with_cursor_success() {
        // Given
        TransactionFilterRequestModel request = new TransactionFilterRequestModel();
        PaginationRequestModel pagination = new PaginationRequestModel();
        pagination.setPageSize(10);
        request.setPagination(pagination);

        TransactionDetailEntity entity = new TransactionDetailEntity();
        entity.setId(java.util.UUID.randomUUID());
        entity.setSequenceNumber(100L);

        Page<TransactionDetailEntity> page = new PageImpl<>(Collections.singletonList(entity));

        when(transactionRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        // When
        TransactionFilterResponseModel response =
                transactionService.filterTransactionsWithCursor(request);

        // Then
        assertNotNull(response);
        assertEquals(1, response.getData().size());
        assertEquals(1, response.getTotalItems());
        assertEquals(100L, response.getData().get(0).getSequenceNumber());
        assertEquals(100L, response.getNextPageToken());
        assertEquals(100L, response.getPreviousPageToken());
    }
}
