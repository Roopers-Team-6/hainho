package com.loopers.domain.user;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.user.vo.BirthDate;
import com.loopers.domain.user.vo.Email;
import com.loopers.domain.user.vo.LoginId;
import com.loopers.domain.user.vo.Point;
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
    private LoginId loginId;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    @Embedded
    private Email email;
    @Embedded
    private BirthDate birthDate;
    @Embedded
    private Point point;

    private User(LoginId loginId, Gender gender, Email email, BirthDate birthDate, Point point) {
        this.loginId = loginId;
        this.gender = gender;
        this.email = email;
        this.birthDate = birthDate;
        this.point = point;
    }

    public static User register(String loginId, String gender, String email, String birthDate) {
        return new User(
                LoginId.of(loginId),
                Gender.from(gender),
                Email.of(email),
                BirthDate.of(birthDate),
                Point.ZERO
        );
    }

    public void chargePoint(long point) {
        this.point = this.point.charge(point);
    }

    public void usePoint(Long point) {
        this.point = this.point.use(point);
    }
}
