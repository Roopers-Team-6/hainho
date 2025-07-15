package com.loopers.domain.user;

public class UserFixture {
    public static final String VALID_USER_ID = "qwer1234";
    public static final String VALID_GENDER = Gender.M.toString();
    public static final String VALID_EMAIL = "email@gmail.com";
    public static final String VALID_BIRTH_DATE = "1990-01-01";

    public static UserCommand.UserRegisterCommand createUserRegisterCommand() {
        return createUserRegisterCommand(VALID_USER_ID, VALID_GENDER, VALID_EMAIL, VALID_BIRTH_DATE);
    }

    public static UserCommand.UserRegisterCommand createUserRegisterCommand(String userId, String gender, String email, String birthDate) {
        return new UserCommand.UserRegisterCommand(userId, gender, email, birthDate);
    }

    public static User createUser(String userId, String gender, String email, String birthDate) {
        UserCommand.UserRegisterCommand command = createUserRegisterCommand(userId, gender, email, birthDate);
        return User.register(command);
    }

    public static User createUserWithEmail(String email) {
        return createUser(VALID_USER_ID, VALID_GENDER, email, VALID_BIRTH_DATE);
    }

    public static User createUserWithGender(String gender) {
        return createUser(VALID_USER_ID, gender, VALID_EMAIL, VALID_BIRTH_DATE);
    }

    public static User createUserWithBirthDate(String birthDate) {
        return createUser(VALID_USER_ID, VALID_GENDER, VALID_EMAIL, birthDate);
    }

    public static User createUserWithUserId(String userId) {
        return createUser(userId, VALID_GENDER, VALID_EMAIL, VALID_BIRTH_DATE);
    }
}