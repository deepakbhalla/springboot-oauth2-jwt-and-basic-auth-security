package com.example.security.deserializer;

import com.example.security.entity.AccountEntity;
import com.example.security.model.Account;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountDetailsDeserializer {

    public Account deserializeAccount(AccountEntity accountEntity) {
        return Account.builder()
                .accountNumber(accountEntity.getAccNo())
                .accountHolderName(accountEntity.getHolderName())
                .accountStartDate(accountEntity.getStartDate())
                .accountBranch(accountEntity.getBranch())
                .accountBalance(accountEntity.getBalance())
                .accountTransactions(accountEntity.getTransactions())
                .build();
    }

    public List<Account> deserializeAccounts(List<AccountEntity> accountEntities) {
        List<Account> accounts = new ArrayList<>();
        accountEntities.forEach(entity -> accounts.add(this.deserializeAccount(entity)));
        return accounts;
    }
}
