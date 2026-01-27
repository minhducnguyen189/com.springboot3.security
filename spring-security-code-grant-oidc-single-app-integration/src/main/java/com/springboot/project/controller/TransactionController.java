package com.springboot.project.controller;

import com.springboot.project.generated.api.TransactionApi;
import com.springboot.project.generated.model.TransactionFilterRequestModel;
import com.springboot.project.generated.model.TransactionFilterResponseModel;
import com.springboot.project.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController implements TransactionApi {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public ResponseEntity<TransactionFilterResponseModel> filterTransactions(
            TransactionFilterRequestModel transactionFilterRequestModel) {
        return new ResponseEntity<>(
                this.transactionService.filterTransactions(transactionFilterRequestModel),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TransactionFilterResponseModel> filterTransactionsWithCursor(
            TransactionFilterRequestModel transactionFilterRequestModel) {
        return new ResponseEntity<>(
                this.transactionService.filterTransactionsWithCursor(transactionFilterRequestModel),
                HttpStatus.OK);
    }
}
