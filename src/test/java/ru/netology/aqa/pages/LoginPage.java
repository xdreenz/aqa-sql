package ru.netology.aqa.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.aqa.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement loginField = $("[data-test-id = 'login'] input");
    private final SelenideElement passwordField = $("[data-test-id = 'password'] input");
    private final SelenideElement loginButton = $("[data-test-id = 'action-login']");
    private final SelenideElement errorNotification = $("[data-test-id = 'error-notification'] .notification__content");

    public void shouldBeErrorNotification(String text) {
        errorNotification.shouldHave(exactText(text)).shouldBe(visible);
    }

    public VerificationPage validLogin(DataHelper.AuthInfo authInfo) {
        loginField.setValue(authInfo.login());
        passwordField.setValue(authInfo.password());
        loginButton.click();
        return new VerificationPage();
    }
}
