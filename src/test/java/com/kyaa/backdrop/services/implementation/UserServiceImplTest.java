package com.kyaa.backdrop.services.implementation;

import com.kyaa.backdrop.data.dto.requests.AddBankAccountRequest;
import com.kyaa.backdrop.data.dto.requests.GetAccountNameRequest;
import com.kyaa.backdrop.data.dto.requests.RegistrationRequest;
import com.kyaa.backdrop.data.dto.responses.AddBankAccountResponse;
import com.kyaa.backdrop.data.models.User;
import com.kyaa.backdrop.data.repositories.UserRepository;
import com.kyaa.backdrop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest

class UserServiceImplTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    private RegistrationRequest registrationRequest;
    private RegistrationRequest registrationRequestTwo;
    private GetAccountNameRequest getAccountNameRequest;

    private AddBankAccountRequest addBankAccountRequest;
    private AddBankAccountRequest addBankAccountRequestTwo;
    @BeforeEach
    void setUp() {
        registrationRequest = new RegistrationRequest("Yusuf Kabir Adekunle");

    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }
    @Test
    void testThatNumberOfUserIncreasesByOneWhenEverANewUserRegisters(){
        int numberOfUsersBeforeANewUserRegisters = userRepository.findAll().size();
        assertEquals(0,numberOfUsersBeforeANewUserRegisters );
        userService.register(registrationRequest);
        int numberOfUsersAfterANewUserRegisters = userRepository.findAll().size();
        assertEquals(1, numberOfUsersAfterANewUserRegisters);
    }

    @Test
    void testThatTrueIsReturnedIfTheDistanceBetweenTwoNamesIsLessOrEqualTwo(){
    String firstName = "Yusuf Kabir Adekunle";
    String secondName = "Yusf Kabir Adeknle";
    assertTrue(userService.verifyNames(firstName,secondName));
}
    @Test
    void testThatFalseIsReturnedIfTheDistanceBetweenTwoNamesIsGreaterThanTwo(){
        String firstName = "Yusuf Kabir Adekunle";
        String secondName = "Yusf Kabir Adknle";
        assertFalse(userService.verifyNames(firstName,secondName));
    }
    @Test
    void testThatUserIsVerifiedIfTheAccountNameProvidedByTheUserEqualsTheAccountNameWithRespectToTheAccountNumberAndBankCode() throws IOException {

        addBankAccountRequest = new AddBankAccountRequest(
                "3046553253",
                "011",
                "Yusuf Kabir Adekunle");
        userService.register(registrationRequest);
        User foundUserBeforeAddingBankAccount = userService.findUserByName("Yusuf Kabir Adekunle");
        assertFalse(foundUserBeforeAddingBankAccount.isVerified());
        userService.addBankAccount(addBankAccountRequest);
        User foundUserAfterAddingBankAccount = userService.findUserByName("Yusuf Kabir Adekunle");
        assertTrue(foundUserAfterAddingBankAccount.isVerified());
    }
    @Test
    void testThatUserIsVerifiedIfTheDistanceBetweenAccountNameProvidedAndTheAccountNameReturnedWithRespectToTheAccountNumberAndBankCodeIsLessOrEqualTwo() throws IOException {
        registrationRequestTwo = new RegistrationRequest("Yusf Kabr Adekunle");
        userService.register(registrationRequestTwo);
        addBankAccountRequestTwo = new AddBankAccountRequest(
                "3046553253",
                "011",
                "Yusf Kabr Adekunle");
        User foundUserBeforeAddingBankAccount = userService.findUserByName("Yusf Kabr Adekunle");
        assertFalse(foundUserBeforeAddingBankAccount.isVerified());
        userService.addBankAccount(addBankAccountRequestTwo);
        User foundUserAfterAddingBankAccount = userService.findUserByName("Yusf Kabr Adekunle");
        assertTrue(foundUserAfterAddingBankAccount.isVerified());
    }
    @Test
    void testThatNameProvidedByUserIsReturnedIfTheDistanceBetweenAccountNameProvidedAndTheAccountNameReturnedWithRespectToTheAccountNumberAndBankCodeIsLessOrEqualTwo() throws IOException{
        getAccountNameRequest = new GetAccountNameRequest(
                "011",
                "3046553253",
                "Yusf Kabr Adekunle");
        String accountName = userService.getAccountName(getAccountNameRequest);
        assertEquals("Yusf Kabr Adekunle", accountName);
    }
    @Test
    void testThatNameFetchedByAPIIsReturnedIfUserDoesntProvideAnAccountName() throws IOException{
        getAccountNameRequest = new GetAccountNameRequest(
                "011",
                "3046553253", null);
        String accountName = userService.getAccountName(getAccountNameRequest);
        assertEquals("YUSUF KABIR ADEKUNLE", accountName);
    }

}