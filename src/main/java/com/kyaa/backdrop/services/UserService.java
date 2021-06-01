package com.kyaa.backdrop.services;

import com.kyaa.backdrop.data.dto.requests.AddBankAccountRequest;
import com.kyaa.backdrop.data.dto.requests.GetAccountNameRequest;
import com.kyaa.backdrop.data.dto.requests.RegistrationRequest;
import com.kyaa.backdrop.data.dto.responses.AddBankAccountResponse;
import com.kyaa.backdrop.data.dto.responses.RegistrationResponse;
import com.kyaa.backdrop.data.models.User;

import java.io.IOException;
import java.util.Optional;

public interface UserService {
    RegistrationResponse register(RegistrationRequest registrationRequest);
    String addBankAccount(AddBankAccountRequest addBankAccountRequest) throws IOException;
    User findUserByName(String name);
    boolean verifyNames(String firstName, String secondName);
    String getAccountName(GetAccountNameRequest getAccountNameRequest) throws IOException;

}
