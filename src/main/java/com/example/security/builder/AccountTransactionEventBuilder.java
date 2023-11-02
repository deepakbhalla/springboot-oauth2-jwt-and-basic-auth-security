package com.example.security.builder;

import com.example.security.constant.AccountConstants;
import com.example.security.model.Transaction;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Creates different events for an account.
 */
public class AccountTransactionEventBuilder {

    public static List<Transaction> createAccountSuccessfulEvent() {
        return Collections.singletonList(Transaction.builder()
                .type(AccountConstants.EVENT_CREATE_ACCOUNT.getMessage())
                .ts(LocalDateTime.now().toString())
                .balance(0L)
                .build());
    }

    public static List<Transaction> updateAccountSuccessfulEvent(List<Transaction> existingTransactions) {
        existingTransactions.sort(Comparator.comparing(Transaction::getTs).reversed());
        Transaction accountUpdateNewEvent = Transaction.builder()
                .type(AccountConstants.EVENT_ACCOUNT_UPDATE.getMessage())
                .ts(LocalDateTime.now().toString())
                .build();
        existingTransactions.add(accountUpdateNewEvent);
        return existingTransactions;
    }

    public static List<Transaction> createDepositSuccessfulEvent(List<Transaction> existingTransactions, Integer deposit) {
        existingTransactions.sort(Comparator.comparing(Transaction::getTs).reversed());
        Long newBalance = ((existingTransactions.get(0).getBalance() != null) ? existingTransactions.get(0).getBalance() : 0L) + deposit;
        Transaction depositNewEvent = Transaction.builder()
                .type(AccountConstants.EVENT_DEPOSIT.getMessage())
                .ts(LocalDateTime.now().toString())
                .balance(newBalance)
                .transactionAmt(Long.valueOf(deposit))
                .build();
        existingTransactions.add(depositNewEvent);
        return existingTransactions;
    }

    public static List<Transaction> createWithdrawalSuccessfulEvent(List<Transaction> existingTransactions, Integer withdrawal) {
        existingTransactions.sort(Comparator.comparing(Transaction::getTs).reversed());
        Long newBalance = ((existingTransactions.get(0).getBalance() != null) ? existingTransactions.get(0).getBalance() : 0L) - withdrawal;
        Transaction depositCurrentEvent = Transaction.builder()
                .type(AccountConstants.EVENT_WITHDRAWAL.getMessage())
                .ts(LocalDateTime.now().toString())
                .balance(newBalance)
                .transactionAmt(Long.valueOf(withdrawal))
                .build();
        existingTransactions.add(depositCurrentEvent);
        return existingTransactions;
    }
}
