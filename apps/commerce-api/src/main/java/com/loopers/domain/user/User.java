package com.loopers.domain.user;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Embedded
    private UserId userId;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    @Embedded
    private Email email;
    @Embedded
    private BirthDate birthDate;

    private User(UserId userId, Gender gender, Email email, BirthDate birthDate) {
        this.userId = userId;
        this.gender = gender;
        this.email = email;
        this.birthDate = birthDate;
    }

    public static User register(UserCommand.UserRegisterCommand command) {
        return new User(
                new UserId(command.userId()),
                Gender.from(command.gender()),
                new Email(command.email()),
                new BirthDate(command.birthDate())
        );
    }
}
