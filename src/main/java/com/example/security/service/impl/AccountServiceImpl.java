package com.example.security.service.impl;

import com.example.security.builder.AccountTransactionEventBuilder;
import com.example.security.constant.AccountConstants;
import com.example.security.deserializer.AccountDetailsDeserializer;
import com.example.security.entity.AccountEntity;
import com.example.security.exception.BadRequestException;
import com.example.security.exception.InsufficientAccountBalanceException;
import com.example.security.exception.ResourceNotFoundException;
import com.example.security.model.Account;
import com.example.security.model.Transaction;
import com.example.security.repository.AccountRepository;
import com.example.security.serializer.AccountDetailsSerializer;
import com.example.security.service.AccountService;
import io.micrometer.common.util.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountDetailsDeserializer accountDetailsDeserializer;

    @Autowired
    AccountDetailsSerializer accountDetailsSerializer;

    @Value("${SERVICE_ACCOUNT}")
    private String serviceAccount;

    @Override
    public Account getAccountInformation(String accountNumber) throws ResourceNotFoundException, BadRequestException {
        if (StringUtils.isBlank(accountNumber) || !NumberUtils.isDigits(accountNumber)) {
            throw new BadRequestException(AccountConstants.PROVIDE_VALID_INPUTS.getMessage() + AccountConstants.ACCOUNT_NUMBER.getMessage());
        }
        Optional<AccountEntity> byAccNo = Optional.ofNullable(this.accountRepository.findByAccNo(Integer.parseInt(accountNumber)));
        if (byAccNo.isEmpty()) {
            throw new ResourceNotFoundException(AccountConstants.ACCOUNT_NOT_FOUND.getMessage());
        }
        AccountEntity accountInformation = byAccNo.get();
        byAccNo.get().getTransactions().sort(Comparator.comparing(Transaction::getTs));
        return this.accountDetailsDeserializer.deserializeAccount(byAccNo.get());
    }

    @Override
    public Account createAccount(Account account) throws BadRequestException {
        StringJoiner stringJoiner = new StringJoiner(AccountConstants.COMMA.getMessage());
        if (StringUtils.isBlank(account.getAccountHolderName())) {
            stringJoiner.add(AccountConstants.ACCOUNT_HOLDER_NAME.getMessage());
        }
        if (StringUtils.isBlank(account.getAccountBranch())) {
            stringJoiner.add(AccountConstants.ACCOUNT_BRANCH.getMessage());
        }

        if (stringJoiner.length() > 0) {
            throw new BadRequestException(AccountConstants.PROVIDE_VALID_INPUTS.getMessage() + stringJoiner);
        }

        AccountEntity accountEntity = this.accountDetailsSerializer.serializeAccount(account);
        return this.accountDetailsDeserializer.deserializeAccount(this.accountRepository.save(accountEntity));
    }

    @Override
    public Account updateAccountBranch(String accountNumber, String newBranch) throws ResourceNotFoundException, BadRequestException {
        StringJoiner stringJoiner = new StringJoiner(AccountConstants.COMMA.getMessage());
        if (StringUtils.isBlank(accountNumber) || !NumberUtils.isDigits(accountNumber)) {
            stringJoiner.add(AccountConstants.ACCOUNT_NUMBER.getMessage());
        }
        if (StringUtils.isBlank(newBranch)) {
            stringJoiner.add(AccountConstants.NEW_BRANCH.getMessage());
        }
        if (stringJoiner.length() > 0) {
            throw new BadRequestException(AccountConstants.PROVIDE_VALID_INPUTS.getMessage() + stringJoiner);
        }
        Optional<AccountEntity> byAccNo = Optional.ofNullable(this.accountRepository.findByAccNo(Integer.parseInt(accountNumber)));
        if (byAccNo.isEmpty()) {
            throw new ResourceNotFoundException(AccountConstants.ACCOUNT_NOT_FOUND.getMessage());
        }
        AccountEntity existingAccount = byAccNo.get();
        existingAccount.setBranch(newBranch);
        existingAccount.setTransactions(AccountTransactionEventBuilder
                .updateAccountSuccessfulEvent(existingAccount.getTransactions()));
        existingAccount.setModifiedDate(new Date());
        existingAccount.setModifiedBy(this.serviceAccount);
        AccountEntity updatedAccount = this.accountRepository.save(existingAccount);
        updatedAccount.getTransactions().sort(Comparator.comparing(Transaction::getTs));
        return this.accountDetailsDeserializer.deserializeAccount(updatedAccount);
    }

    @Override
    public void deleteAccount(String accountNumber) throws ResourceNotFoundException, BadRequestException {
        if (StringUtils.isBlank(accountNumber) || !NumberUtils.isDigits(accountNumber)) {
            throw new BadRequestException(AccountConstants.PROVIDE_VALID_INPUTS.getMessage() + AccountConstants.ACCOUNT_NUMBER.getMessage());
        }
        Optional<AccountEntity> byAccNo = Optional.ofNullable(this.accountRepository.findByAccNo(Integer.parseInt(accountNumber)));
        if (byAccNo.isEmpty()) {
            throw new ResourceNotFoundException(AccountConstants.ACCOUNT_NOT_FOUND.getMessage());
        }
        this.accountRepository.deleteByAccNo(Integer.parseInt(accountNumber));
    }

    @Override
    public Account deposit(String accountNumber, String depositAmount) throws ResourceNotFoundException, BadRequestException {
        StringJoiner stringJoiner = new StringJoiner(AccountConstants.COMMA.getMessage());
        if (StringUtils.isBlank(accountNumber) || !NumberUtils.isDigits(accountNumber)) {
            stringJoiner.add(AccountConstants.ACCOUNT_NUMBER.getMessage());
        }
        if (StringUtils.isBlank(depositAmount) || !NumberUtils.isDigits(depositAmount)) {
            stringJoiner.add(AccountConstants.DEPOSIT_AMOUNT.getMessage());
        }
        if (stringJoiner.length() > 0) {
            throw new BadRequestException(AccountConstants.PROVIDE_VALID_INPUTS.getMessage() + stringJoiner);
        }

        Optional<AccountEntity> byAccNo = Optional.ofNullable(this.accountRepository.findByAccNo(Integer.parseInt(accountNumber)));
        if (byAccNo.isEmpty()) {
            throw new ResourceNotFoundException(AccountConstants.ACCOUNT_NOT_FOUND.getMessage());
        }
        AccountEntity existingAccount = byAccNo.get();
        int deposit = Integer.parseInt(depositAmount);
        Long newBalance = ((existingAccount.getBalance() != null) ? existingAccount.getBalance() : 0L) + deposit;
        existingAccount.setBalance(newBalance);
        existingAccount.setModifiedDate(new Date());
        existingAccount.setModifiedBy(this.serviceAccount);
        existingAccount.setTransactions(AccountTransactionEventBuilder
                .createDepositSuccessfulEvent(existingAccount.getTransactions(), deposit));
        AccountEntity updatedAccount = this.accountRepository.save(existingAccount);
        updatedAccount.getTransactions().sort(Comparator.comparing(Transaction::getTs));
        return this.accountDetailsDeserializer.deserializeAccount(updatedAccount);
    }

    @Override
    public Account withdraw(String accountNumber, String withdrawalAmount) throws ResourceNotFoundException, BadRequestException, InsufficientAccountBalanceException {
        StringJoiner stringJoiner = new StringJoiner(AccountConstants.COMMA.getMessage());
        if (StringUtils.isBlank(accountNumber) || !NumberUtils.isDigits(accountNumber)) {
            stringJoiner.add(AccountConstants.ACCOUNT_NUMBER.getMessage());
        }
        if (StringUtils.isBlank(withdrawalAmount) || !NumberUtils.isDigits(withdrawalAmount)) {
            stringJoiner.add(AccountConstants.DEPOSIT_AMOUNT.getMessage());
        }
        if (stringJoiner.length() > 0) {
            throw new BadRequestException(AccountConstants.PROVIDE_VALID_INPUTS.getMessage() + stringJoiner);
        }

        Optional<AccountEntity> byAccNo = Optional.ofNullable(this.accountRepository.findByAccNo(Integer.parseInt(accountNumber)));
        if (byAccNo.isEmpty()) {
            throw new ResourceNotFoundException(AccountConstants.ACCOUNT_NOT_FOUND.getMessage());
        }
        AccountEntity existingAccount = byAccNo.get();
        Integer withdraw = Integer.parseInt(withdrawalAmount);

        Long existingBalance = ((existingAccount.getBalance() != null) ? existingAccount.getBalance() : 0L);
        if (Long.valueOf(withdraw) > existingBalance) {
            throw new InsufficientAccountBalanceException(AccountConstants.INSUFFICIENT_ACCOUNT_BALANCE.getMessage());
        }

        Long newBalance = existingBalance - withdraw;
        existingAccount.setBalance(newBalance);
        existingAccount.setModifiedDate(new Date());
        existingAccount.setModifiedBy(this.serviceAccount);
        existingAccount.setTransactions(AccountTransactionEventBuilder
                .createWithdrawalSuccessfulEvent(existingAccount.getTransactions(), withdraw));
        AccountEntity updatedAccount = this.accountRepository.save(existingAccount);
        updatedAccount.getTransactions().sort(Comparator.comparing(Transaction::getTs));
        return this.accountDetailsDeserializer.deserializeAccount(updatedAccount);
    }

    @Override
    public List<Account> getAllAccounts() {
        return this.accountDetailsDeserializer.deserializeAccounts(this.accountRepository.findAll());
    }
}
