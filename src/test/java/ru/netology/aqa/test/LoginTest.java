package ru.netology.aqa.test;

import org.junit.jupiter.api.*;
import ru.netology.aqa.data.DataHelper;
import ru.netology.aqa.pages.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.aqa.data.SQLHelper.*;

public class LoginTest {

    LoginPage loginPage;

    @AfterEach
    void tearDownCodes() {
        cleanAuthCodes();
    }

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }

    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @Test
    @DisplayName("Should be success when logging in with a test user")
    void shouldBeSuccess1() {
        var authInfo = DataHelper.getTestUser();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.shouldBeVisible();
        var verificationCode = getVerificationCode();
        verificationPage.validVerify(verificationCode.code());
    }

    @Test
    @DisplayName("Should be success when logging in with a registered new user.")
    void shouldBeSuccess2() {
        var authInfo = DataHelper.generateValidNewUser(DataHelper.generateRandomLogin());
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.shouldBeVisible();
        var verificationCode = getVerificationCode();
        verificationPage.validVerify(verificationCode.code());
    }

    @Test
    @DisplayName("Should be an error when logging in with an unregistered user")
    void shouldBeError1() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.shouldBeErrorNotification("Ошибка! \nНеверно указан логин или пароль");
    }

    @Test
    @DisplayName("Should be an error when logging in with a valid user, but an invalid verification code")
    void shouldBeError2() {
        var authInfo = DataHelper.generateValidNewUser(DataHelper.generateRandomLogin());
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.shouldBeVisible();
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.codeVerify(verificationCode.code());
        verificationPage.shouldBeErrorNotification("Ошибка! \nНеверно указан код! Попробуйте ещё раз.");
    }

    @Test
    @DisplayName("The user should be blocked if he made 3 attempts to login with an invalid password")
    void shouldBeBlocked() {
        var userName = DataHelper.generateRandomLogin();
        DataHelper.generateValidNewUser(userName);
        var newUserWithInvalidPass = new DataHelper.AuthInfo(userName, DataHelper.generateRandomPassword()); //Тот же юзер, но с заведомо неправильным паролем
        loginPage.validLogin(newUserWithInvalidPass);
        loginPage.validLogin(newUserWithInvalidPass);
        loginPage.validLogin(newUserWithInvalidPass);
        //loginPage.shouldBeErrorNotification("Вы были заблокированы");
        Assertions.assertEquals("blocked", getUserStatus(userName));
    }

}
