package com.example.security.serializer;

import com.example.security.builder.AccountTransactionEventBuilder;
import com.example.security.entity.AccountEntity;
import com.example.security.model.Account;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AccountDetailsSerializer {

    @Value("${SERVICE_ACCOUNT}")
    private String serviceAccount;

    public AccountEntity serializeAccount(Account account) {
        return AccountEntity.builder()
                .accNo(Integer.valueOf(RandomStringUtils.randomNumeric(5, 5)))
                .holderName(account.getAccountHolderName())
                .startDate(new Date())
                .branch(account.getAccountBranch())
                .balance(0L)
                .transactions(AccountTransactionEventBuilder.createAccountSuccessfulEvent())
                .createdBy(this.serviceAccount)
                .createdDate(new Date())
                .build();
    }
}
