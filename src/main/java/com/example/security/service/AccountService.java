package com.example.security.service;

import com.example.security.exception.BadRequestException;
import com.example.security.exception.InsufficientAccountBalanceException;
import com.example.security.exception.ResourceNotFoundException;
import com.example.security.model.Account;

import java.util.List;

public interface AccountService {

    public Account getAccountInformation(String accountNumber) throws ResourceNotFoundException, BadRequestException;

    Account createAccount(Account account) throws BadRequestException;

    Account updateAccountBranch(String accountNumber, String newBranch) throws ResourceNotFoundException, BadRequestException;

    void deleteAccount(String accountNumber) throws ResourceNotFoundException, BadRequestException;

    Account deposit(String accountNumber, String depositAmount) throws ResourceNotFoundException, BadRequestException;

    Account withdraw(String accountNumber, String withdrawalAmount) throws ResourceNotFoundException, BadRequestException, InsufficientAccountBalanceException;

    List<Account> getAllAccounts();
}
