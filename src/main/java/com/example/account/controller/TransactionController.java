package com.example.account.controller;

import com.example.account.domain.Account;
import com.example.account.dto.TransactionDto;
import com.example.account.dto.UseBalance;
import com.example.account.exception.AccountException;
import com.example.account.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 잔액 관련 컨트롤러
 * 1. 잔액사용
 * 2. 잔액사용 취소
 * 3. 거래 확인

 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/transaction/use")
    public UseBalance.Response useBalance(
            @Valid@RequestBody UseBalance.Request request
            ){
        try {
            return UseBalance.Response.from(transactionService.useBalance(
                    request.getUserId(),
                    request.getAccountNumber(),
                    request.getAmount()));
        }catch (AccountException e){
            log.error("Failed to use balance.");

            transactionService.saveFailedUseTransaction(
                    request.getAccountNumber(),
                    request.getAmount()
            );

            throw e;
        }
    }
}
