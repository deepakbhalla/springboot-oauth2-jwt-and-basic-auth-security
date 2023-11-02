package com.example.security.constant;

public enum AccountConstants {

    SERVICE_IS_RUNNING("service is up and running"),
    DELETED("deleted"),
    ACCOUNT_HOLDER_NAME("accountHolderName"),
    ACCOUNT_BRANCH("accountBranch"),
    ACCOUNT_NUMBER("accountNumber"),
    DEPOSIT_AMOUNT("depositAmount"),
    NEW_BRANCH("newBranch"),
    PROVIDE_VALID_INPUTS("Provide the mandatory request input(s): "),
    ACCOUNT_NOT_FOUND("Account not found."),
    EVENT_CREATE_ACCOUNT("create_account"),
    EVENT_ACCOUNT_UPDATE("account_update"),
    EVENT_DEPOSIT("deposit"),
    EVENT_WITHDRAWAL("withdrawal"),
    COMMA(","),
    INSUFFICIENT_ACCOUNT_BALANCE("Withdrawal amount is greater than the available account balance."),
    USER_NOT_FOUND("User not found.");

    public final String message;

    AccountConstants(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return this.message;
    }
}
