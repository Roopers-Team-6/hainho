package com.loopers.domain.user;

import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @Nested
    @DisplayName("유저를 조회할 때")
    class FindUser {

        @Test
        @DisplayName("존재하는 user.id로 조회하면, Optional<User>를 반환한다.")
        void findExistingUser() {
            // Given
            User existingUser = userRepository.save(UserFixture.createUser());
            long userId = existingUser.getId();

            // When
            User foundUser = userService.find(userId).get();

            // Then
            assertAll(
                    () -> assertThat(foundUser).isNotNull(),
                    () -> assertThat(foundUser.getId()).isEqualTo(existingUser.getId())
            );
        }

        @Test
        @DisplayName("존재하지 않는 user.id로 조회하면, 빈 Optional을 반환한다.")
        void findNonExistingUser() {
            // Given
            long nonExistingUserId = 1L;

            // When
            Optional<User> result = userService.find(nonExistingUserId);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("포인트를 조회할 때")
    class FindPoint {

        @Test
        @DisplayName("존재하는 user.id로 조회하면, Optional<Point>를 반환한다.")
        void findExistingUserPoint() {
            // Given
            User existingUser = userRepository.save(UserFixture.createUser());
            long userId = existingUser.getId();

            // When
            Optional<Point> foundPoint = userService.findPoint(userId);

            // Then
            assertAll(
                    () -> assertThat(foundPoint).hasValue(existingUser.getPoint())
            );
        }

        @Test
        @DisplayName("존재하지 않는 user.id로 조회하면, 빈 Optional을 반환한다.")
        void findNonExistingUserPoint() {
            // Given
            long nonExistingUserId = 1L;

            // When
            Optional<Point> result = userService.findPoint(nonExistingUserId);

            // Then
            assertThat(result).isEmpty();
        }
    }
}