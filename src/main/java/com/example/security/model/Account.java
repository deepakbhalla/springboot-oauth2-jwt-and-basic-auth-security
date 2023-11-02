package com.example.security.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"timestamp", "accountNumber", "accountHolderName", "accountBalance", "accountStartDate",
        "accountBranch", "accountTransactions"})
public class Account implements Serializable {

    @Serial
    private static final long serialVersionUID = 5865597278076349945L;

    private Date timestamp;
    private Integer accountNumber;
    private String accountHolderName;
    private Long accountBalance;
    private Date accountStartDate;
    private String accountBranch;
    private List<Transaction> accountTransactions;
}
