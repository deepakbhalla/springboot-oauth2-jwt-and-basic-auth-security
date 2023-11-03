package com.example.security.controllers;

import com.example.security.constant.AccountConstants;
import com.example.security.exception.BadRequestException;
import com.example.security.exception.ResourceNotFoundException;
import com.example.security.model.Account;
import com.example.security.model.DeleteAccount;
import com.example.security.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Account management controller.
 */
@Tag(name = "Account Management", description = "Account Management APIs")
@RestController
@RequestMapping("/api/account")
public class AccountManagementController {

    @Autowired
    AccountService accountService;

    @Operation(summary = "Get all accounts")
    @ApiResponse(responseCode = "200", description = "All existing accounts",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))})
    @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = {@Content})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public List<Account> getAllAccounts() {
        return this.accountService.getAllAccounts();
    }

    /**
     * Create new account.
     *
     * @param account - Account - Account to be created
     * @return Account - Created account
     */
    @Operation(summary = "Create an account")
    @ApiResponse(responseCode = "200", description = "Account created",
        content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))})
    @ApiResponse(responseCode = "400", description = "Invalid request body", content = {@Content})
    @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = {@Content})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Account createAccount(
            @RequestBody(description = "Request body takes only account holder name 'accountHolderName' and account branch 'accountBranch' to create a new account", required = true,
                content = @Content(schema = @Schema(implementation = Account.class)))
            @org.springframework.web.bind.annotation.RequestBody Account account) throws BadRequestException {
        return this.accountService.createAccount(account);
    }

    /**
     * Get account information.
     *
     * @param accountNumber - Integer - Account number
     * @return Account - Existing account details
     */
    @Operation(summary = "Get an account details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found an account",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid account number supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content)})
    @GetMapping(value = "/{accountNumber}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public Account getAccountBalance(
            @Parameter(description = "Account number to be searched")
            @PathVariable("accountNumber") String accountNumber) throws ResourceNotFoundException, BadRequestException {
        return this.accountService.getAccountInformation(accountNumber);
    }

    /**
     * Updates branch name of the existing account.
     *
     * @param accountNumber - Integer - Existing account number
     * @param newBranch     - String - Name of new branch
     * @return Account - Updated account details
     */
    @Operation(summary = "Update an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid account number and/or new branch name supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content)})
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public Account updateAccountBranch(
            @Parameter(description = "Account to be updated")
            @RequestParam("accountNumber") String accountNumber,
            @Parameter(description = "New branch name to be updated")
            @RequestParam("newBranch") String newBranch) throws ResourceNotFoundException, BadRequestException {
        return this.accountService.updateAccountBranch(accountNumber, newBranch);
    }

    /**
     * Delete an account.
     *
     * @param accountNumber - Integer - Existing account number
     * @return DeleteAccount - Delete account status
     */
    @Operation(summary = "Delete an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted an account",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid account number supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content)})
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<DeleteAccount> deleteAccount(
            @Parameter(description = "Account number to be searched")
            @RequestParam("accountNumber") String accountNumber) throws ResourceNotFoundException, BadRequestException {
        this.accountService.deleteAccount(accountNumber);
        return new ResponseEntity<>(DeleteAccount.builder()
                .timestamp(LocalDateTime.now())
                .accountNumber(Integer.parseInt(accountNumber))
                .status(AccountConstants.DELETED.getMessage())
                .build(), HttpStatus.OK);
    }
}
