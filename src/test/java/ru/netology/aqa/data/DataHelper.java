package ru.netology.aqa.data;

import net.datafaker.Faker;

public class DataHelper {
    private static final Faker faker = new Faker();

    private DataHelper() {
    }

    public static String generateRandomLogin() {
        return faker.name().name();
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

    public record AuthInfo(
            String login,
            String password
    ) {
    }

    public record VerificationCode(
            String code
    ) {
    }
}
