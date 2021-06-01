package com.kyaa.backdrop.controllers;

import com.kyaa.backdrop.data.dto.requests.AddBankAccountRequest;
import com.kyaa.backdrop.data.dto.requests.GetAccountNameRequest;
import com.kyaa.backdrop.data.dto.requests.RegistrationRequest;
import com.kyaa.backdrop.data.dto.responses.RegistrationResponse;
import com.kyaa.backdrop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @MutationMapping
    public RegistrationResponse registerUser(@Argument RegistrationRequest registrationRequest){
        return userService.register(registrationRequest);
    }
    @MutationMapping
    public String addBankAccount(@Argument AddBankAccountRequest addBankAccountRequest) throws IOException {
        return userService.addBankAccount(addBankAccountRequest);
    }
    @QueryMapping
    public String getAccountName(@Argument GetAccountNameRequest getAccountNameRequest) throws IOException {
        return userService.getAccountName(getAccountNameRequest);
    }
}
