package com.kyaa.backdrop.data.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Setter
@Getter
public class GetAccountNameRequest {
    private String bankCode;
    private String accountNumber;
    private String accountName;

    public GetAccountNameRequest(String bankCode, String accountNumber, String accountName) {
        this.bankCode = bankCode;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
    }

//    public GetAccountNameRequest(String bankCode, String accountNumber) {
//        this.bankCode = bankCode;
//        this.accountNumber = accountNumber;
//    }
}
