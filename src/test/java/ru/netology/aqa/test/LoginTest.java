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
    @DisplayName("Should be success when login with test user")
    void shouldLoginSuccess1() {
        var authInfo = DataHelper.getTestUser();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.shouldBeVisible();
        var verificationCode = getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    @DisplayName("Should be success when login with registered new user")
    void shouldLoginSuccess2() {
        var authInfo = DataHelper.generateValidNewUser(DataHelper.generateRandomLogin());
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.shouldBeVisible();
        var verificationCode = getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    @DisplayName("Should be error when login with unregistered user")
    void shouldBeError1() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.shouldBeErrorNotification("Ошибка! \nНеверно указан логин или пароль");
    }

    @Test
    @DisplayName("Should be error when login with valid user, but invalid verification code")
    void shouldBeError2() {
        var authInfo = DataHelper.generateValidNewUser(DataHelper.generateRandomLogin());
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.shouldBeVisible();
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.codeVerify(verificationCode.getCode());
        verificationPage.shouldBeErrorNotification("Ошибка! \nНеверно указан код! Попробуйте ещё раз.");
    }

    @Test
    @DisplayName("User should be blocked, it he made 3 attempts to login with invalid password")
    void shouldBeBlocked() {
        var newUserName = DataHelper.generateRandomLogin();
        DataHelper.generateValidNewUser(newUserName);
        var newUserWithInvalidPass = new DataHelper.AuthInfo(newUserName, DataHelper.generateRandomPassword()); //Тот же юзер, но с заведомо неправильным паролем
        loginPage.validLogin(newUserWithInvalidPass);
        loginPage.validLogin(newUserWithInvalidPass);
        loginPage.validLogin(newUserWithInvalidPass);
        Assertions.assertEquals("blocked", getUserStatus(newUserName));
    }

}
