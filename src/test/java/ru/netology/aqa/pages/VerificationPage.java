package ru.netology.aqa.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private final SelenideElement codeField = $("[data-test-id = 'code'] input");
    private final SelenideElement verifyButton = $("[data-test-id = 'action-verify']");
    private final SelenideElement errorNotification = $("[data-test-id = 'error-notification'] .notification__content");

    public void shouldBeErrorNotification(String text) {
        errorNotification.shouldHave(exactText(text)).shouldBe(visible);
    }

    public DashboardPage validVerify(String verificationCode) {
        codeVerify(verificationCode);
        return new DashboardPage();
    }

    public void codeVerify(String verificationCode) {
        codeField.setValue(verificationCode);
        verifyButton.click();
    }

    public void shouldBeVisible() {
        codeField.shouldBe(visible);
    }
}
