package ru.netology.aqa.data;

import com.github.javafaker.Faker;
import lombok.Value;

public class DataHelper {
    private static final Faker faker = new Faker();

    private DataHelper() {
    }

    public static String generateRandomLogin() {
        return faker.name().username();
    }

    public static String generateRandomPassword() {
        return faker.internet().password();
    }

    public static AuthInfo generateValidNewUser(String userName) {
        SQLHelper.registerNewUser(userName);
        return new AuthInfo(userName, "qwerty123");
    }

    public static AuthInfo generateRandomUser() {
        return new AuthInfo(generateRandomLogin(), generateRandomPassword());
    }

    public static AuthInfo getTestUser() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static VerificationCode generateRandomVerificationCode() {
        return new VerificationCode(faker.numerify("#####"));
    }

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    @Value
    public static class VerificationCode {
        String code;
    }
}
