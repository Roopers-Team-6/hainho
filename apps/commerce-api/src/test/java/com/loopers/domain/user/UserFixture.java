package com.loopers.domain.user;

public class UserFixture {
    public static final String VALID_LOGIN_ID = "qwer1234";
    public static final String VALID_GENDER = Gender.M.toString();
    public static final String VALID_EMAIL = "email@gmail.com";
    public static final String VALID_BIRTH_DATE = "1990-01-01";

    public static User createUser() {
        return User.register(VALID_LOGIN_ID, VALID_GENDER, VALID_EMAIL, VALID_BIRTH_DATE);
    }

    public static User createUser(String loginId, String gender, String email, String birthDate) {
        return User.register(loginId, gender, email, birthDate);
    }

    public static User createUserWithEmail(String email) {
        return createUser(VALID_LOGIN_ID, VALID_GENDER, email, VALID_BIRTH_DATE);
    }

    public static User createUserWithGender(String gender) {
        return createUser(VALID_LOGIN_ID, gender, VALID_EMAIL, VALID_BIRTH_DATE);
    }

    public static User createUserWithBirthDate(String birthDate) {
        return createUser(VALID_LOGIN_ID, VALID_GENDER, VALID_EMAIL, birthDate);
    }

    public static User createUserWithLoginId(String loginId) {
        return createUser(loginId, VALID_GENDER, VALID_EMAIL, VALID_BIRTH_DATE);
    }
}