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
    @Embedded
    private Point point;

    private User(UserId userId, Gender gender, Email email, BirthDate birthDate, Point point) {
        this.userId = userId;
        this.gender = gender;
        this.email = email;
        this.birthDate = birthDate;
        this.point = point;
    }

    public static User register(String userId, String gender, String email, String birthDate) {
        return new User(
                new UserId(userId),
                Gender.from(gender),
                new Email(email),
                new BirthDate(birthDate),
                Point.ZERO
        );
    }

    public void chargePoint(long point) {
        this.point = this.point.charge(point);
    }
}
