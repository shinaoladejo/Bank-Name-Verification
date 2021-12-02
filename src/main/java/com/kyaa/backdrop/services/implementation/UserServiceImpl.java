package com.kyaa.backdrop.services.implementation;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyaa.backdrop.data.dto.requests.AddBankAccountRequest;
import com.kyaa.backdrop.data.dto.requests.GetAccountNameRequest;
import com.kyaa.backdrop.data.dto.requests.RegistrationRequest;
import com.kyaa.backdrop.data.dto.responses.AddBankAccountResponse;
import com.kyaa.backdrop.data.dto.responses.GetAccountNameResponse;
import com.kyaa.backdrop.data.dto.responses.RegistrationResponse;
import com.kyaa.backdrop.data.models.User;
import com.kyaa.backdrop.data.repositories.UserRepository;
import com.kyaa.backdrop.exceptions.UserException;
import com.kyaa.backdrop.services.UserService;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;
import java.io.IOException;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
//    private final UserRepository userRepository;
    private final String key = "sk_live_477dc8ab7aee59ed474800055c0f90594671514c";
    @Override
    public RegistrationResponse register(RegistrationRequest registrationRequest) {
        if (userRepository.findUserByNameIgnoreCase(registrationRequest.name()).isPresent())throw new UserException("user with " +
                registrationRequest.name()+" already exists");
        User user = User.builder()
                .name(registrationRequest.name())
                .build();
        User savedUser = userRepository.save(user);
        RegistrationResponse response = new RegistrationResponse();
        response.setName(savedUser.getName());
        response.setMessage("Registration was successful");
        return response;
    }

    @Override
    public String addBankAccount(AddBankAccountRequest addBankAccountRequest) throws IOException {
        User foundUser = findUserByName(addBankAccountRequest.accountName());
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.paystack.co/bank/resolve?account_number="
                        +addBankAccountRequest.accountNumber()+"&bank_code="
                        +addBankAccountRequest.bankCode())
                .get()
                .addHeader("Authorization", "Bearer "+ key)
                .build();
        try(ResponseBody responseBody = client.newCall(request).execute().body()) {
            ObjectMapper mapper = new ObjectMapper();
            String returnedAccountName = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(responseBody.string(), AddBankAccountResponse.class).getData().getAccount_name();

            boolean isMatch = verifyNames(addBankAccountRequest.accountName(), returnedAccountName);

            boolean isVerified = foundUser.isVerified();
            if (!isVerified && isMatch)userRepository.updateUser(foundUser.getName());

            return "Verified: "+findUserByName(addBankAccountRequest.accountName()).isVerified();
        }
    }

    @Override
    public User findUserByName(String name) {
        return userRepository.findUserByNameIgnoreCase(name).orElseThrow(()->new UserException("User not found"));
    }

    @Override
    public boolean verifyNames(String firstName, String secondName) {
        LevenshteinDistance ld = new LevenshteinDistance();
        int distance = ld.apply(firstName.toLowerCase(), secondName.toLowerCase());
        return distance<= 2;
    }

    @Override
    public String getAccountName(GetAccountNameRequest getAccountNameRequest) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String nameProvidedByUser = getAccountNameRequest.getAccountName();
        Request request = new Request.Builder()
                .url("https://api.paystack.co/bank/resolve?account_number="
                        +getAccountNameRequest.getAccountNumber()+"&bank_code="
                        +getAccountNameRequest.getBankCode())
                .get()
                .addHeader("Authorization", "Bearer "+key)
                .build();
        try(ResponseBody responseBody = client.newCall(request).execute().body()){
            ObjectMapper mapper = new ObjectMapper();
            String accountName =
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                            .readValue(responseBody.string(), GetAccountNameResponse.class)
                            .getData()
                            .getAccount_name();
            if (nameProvidedByUser!= null && verifyNames(nameProvidedByUser, accountName))return nameProvidedByUser;
            else return accountName;
        }
    }
}
