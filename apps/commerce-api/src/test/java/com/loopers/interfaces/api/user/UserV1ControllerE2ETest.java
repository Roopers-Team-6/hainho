package com.loopers.interfaces.api.user;


import com.loopers.interfaces.api.ApiResponse;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserV1ControllerE2ETest {

    private static final String REGISTER_USER_ENDPOINT = "/api/v1/users";
    private static final String GET_USER_ENDPOINT = "/api/v1/users/me";
    private static final String GET_USER_POINT_ENDPOINT = "/api/v1/points";

    private final TestRestTemplate testRestTemplate;
    private final DatabaseCleanUp databaseCleanUp;

    @Autowired
    public UserV1ControllerE2ETest(
            TestRestTemplate testRestTemplate,
            DatabaseCleanUp databaseCleanUp
    ) {
        this.testRestTemplate = testRestTemplate;
        this.databaseCleanUp = databaseCleanUp;
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @Nested
    @DisplayName("POST /api/v1/users")
    class CreateUser {

        @Test
        @DisplayName("회원 가입이 성공할 경우, 생성된 유저 정보를 반환한다.")
        void createUserSuccess() {
            // arrange
            UserV1Dto.UserRegisterRequest request = new UserV1Dto.UserRegisterRequest(
                    "qwer1234",
                    "M",
                    "email@gmail.com",
                    "2000-01-01"
            );

            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserRegisterResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UserV1Dto.UserRegisterResponse>> response =
                    testRestTemplate.exchange(REGISTER_USER_ENDPOINT, HttpMethod.POST, new HttpEntity<>(request), responseType);

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(response.getBody()).isNotNull(),
                    () -> assertThat(response.getBody().data()).isNotNull(),
                    () -> assertThat(response.getBody().data().id()).isNotNull(),
                    () -> assertThat(response.getBody().data().loginId()).isEqualTo(request.loginId()),
                    () -> assertThat(response.getBody().data().gender()).hasToString(request.gender()),
                    () -> assertThat(response.getBody().data().email()).isEqualTo(request.email()),
                    () -> assertThat(response.getBody().data().birthDate()).isEqualTo(request.birthDate())
            );
        }

        @Test
        @DisplayName("회원 가입 시에 성별이 없을 경우, 400 Bad Request 응답을 반환한다.")
        void createUserWithoutGender() {
            // arrange
            UserV1Dto.UserRegisterRequest request = new UserV1Dto.UserRegisterRequest(
                    "qwer1234",
                    null,
                    "email@gmail.com",
                    "2000-01-01"
            );

            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserRegisterResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<UserV1Dto.UserRegisterResponse>> response =
                    testRestTemplate.exchange(REGISTER_USER_ENDPOINT, HttpMethod.POST, new HttpEntity<>(request), responseType);

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
                    () -> assertThat(response.getBody()).isNotNull()
            );
        }
    }

    @Nested
    @DisplayName("GET /api/v1/users/me")
    class GetUser {

        @Test
        @DisplayName("유저 정보를 조회할 때, 존재하는 user.id로 조회하면 해당 유저 정보를 반환한다.")
        void getUserSuccess() {
            // arrange
            UserV1Dto.UserRegisterRequest registerRequest = new UserV1Dto.UserRegisterRequest(
                    "qwer1234",
                    "M",
                    "email@gmail.com",
                    "2000-01-01"
            );

            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserRegisterResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            var registerResponse = testRestTemplate.exchange(REGISTER_USER_ENDPOINT, HttpMethod.POST, new HttpEntity<>(registerRequest), responseType);

            Long userId = registerResponse.getBody().data().id();
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", userId.toString());

            // act
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
                    testRestTemplate.exchange(GET_USER_ENDPOINT, HttpMethod.GET, new HttpEntity<>(headers),
                            new ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>>() {
                            });

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(response.getBody()).isNotNull(),
                    () -> assertThat(response.getBody().data()).isNotNull(),
                    () -> assertThat(response.getBody().data().loginId()).isEqualTo(registerRequest.loginId()),
                    () -> assertThat(response.getBody().data().gender()).isEqualTo(registerRequest.gender()),
                    () -> assertThat(response.getBody().data().email()).isEqualTo(registerRequest.email()),
                    () -> assertThat(response.getBody().data().birthDate()).isEqualTo(registerRequest.birthDate())
            );
        }

        @Test
        @DisplayName("유저 정보를 조회할 때, 해당 user.id의 유저가 없으면 404 Not Found 응답을 반환한다.")
        void getUserNotFound() {
            // arrange
            Long userId = 1L;
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", userId.toString());

            // act
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
                    testRestTemplate.exchange(GET_USER_ENDPOINT, HttpMethod.GET, new HttpEntity<>(headers),
                            new ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>>() {
                            });

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
                    () -> assertThat(response.getBody()).isNotNull()
            );
        }

    }

    @Nested
    @DisplayName("GET /api/v1/points")
    class GetUserPoint {

        @Test
        @DisplayName("포인트 조회가 성공할 경우, 사용자의 포인트 정보를 반환한다.")
        void getUserPointSuccess() {
            // arrange
            UserV1Dto.UserRegisterRequest registerRequest = new UserV1Dto.UserRegisterRequest(
                    "qwer1234",
                    "M",
                    "email@gmail.com",
                    "2000-01-01"
            );

            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserRegisterResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            var registerResponse = testRestTemplate.exchange(REGISTER_USER_ENDPOINT, HttpMethod.POST, new HttpEntity<>(registerRequest), responseType);

            Long userId = registerResponse.getBody().data().id();
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", userId.toString());

            // act
            ResponseEntity<ApiResponse<UserV1Dto.PointResponse>> response =
                    testRestTemplate.exchange(GET_USER_POINT_ENDPOINT, HttpMethod.GET, new HttpEntity<>(headers),
                            new ParameterizedTypeReference<ApiResponse<UserV1Dto.PointResponse>>() {
                            });
            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(response.getBody()).isNotNull(),
                    () -> assertThat(response.getBody().data()).isNotNull(),
                    () -> assertThat(response.getBody().data().point()).isZero()
            );
        }

        @Test
        @DisplayName("X-USER-ID 헤더가 없을 경우, 400 Bad Request 응답을 반환한다.")
        void getUserPointWithoutHeader() {
            // act
            ResponseEntity<ApiResponse<UserV1Dto.PointResponse>> response =
                    testRestTemplate.exchange(GET_USER_POINT_ENDPOINT, HttpMethod.GET, null,
                            new ParameterizedTypeReference<ApiResponse<UserV1Dto.PointResponse>>() {
                            });

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
                    () -> assertThat(response.getBody()).isNotNull()
            );
        }
    }

    @Nested
    @DisplayName("POST /api/v1/points/charge")
    class ChargeUserPoint {

        private static final String CHARGE_USER_POINT_ENDPOINT = "/api/v1/points/charge";

        @Test
        @DisplayName("존재하는 유저가 1000원을 충전할 경우, 충전된 보유 총량을 응답으로 반환한다.")
        void chargeUserPointSuccess() {
            // arrange
            UserV1Dto.UserRegisterRequest registerRequest = new UserV1Dto.UserRegisterRequest(
                    "qwer1234",
                    "M",
                    "email@gmail.com",
                    "2000-01-01"
            );

            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserRegisterResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            var registerResponse = testRestTemplate.exchange(REGISTER_USER_ENDPOINT, HttpMethod.POST, new HttpEntity<>(registerRequest), responseType);

            Long userId = registerResponse.getBody().data().id();
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", userId.toString());

            ResponseEntity<ApiResponse<UserV1Dto.PointResponse>> getPointResponse =
                    testRestTemplate.exchange(GET_USER_POINT_ENDPOINT, HttpMethod.GET, new HttpEntity<>(headers),
                            new ParameterizedTypeReference<ApiResponse<UserV1Dto.PointResponse>>() {
                            });
            Long initialPoint = getPointResponse.getBody().data().point();
            Long chargingPoint = 1000L;

            UserV1Dto.PointChargeRequest chargeRequest = new UserV1Dto.PointChargeRequest(chargingPoint);

            // act
            ResponseEntity<ApiResponse<UserV1Dto.PointChargeResponse>> response =
                    testRestTemplate.exchange(CHARGE_USER_POINT_ENDPOINT, HttpMethod.POST,
                            new HttpEntity<>(chargeRequest, headers),
                            new ParameterizedTypeReference<ApiResponse<UserV1Dto.PointChargeResponse>>() {
                            });

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(response.getBody()).isNotNull(),
                    () -> assertThat(response.getBody().data()).isNotNull(),
                    () -> assertThat(response.getBody().data().point()).isEqualTo(initialPoint + chargingPoint)
            );
        }

        @Test
        @DisplayName("존재하지 않는 UserId로 포인트 충전 시도 시, 404 Not Found 응답을 반환한다.")
        void chargeUserPointNotFound() {
            // arrange
            Long userId = 1L;
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", userId.toString());

            UserV1Dto.PointChargeRequest chargeRequest = new UserV1Dto.PointChargeRequest(100L);

            // act
            ResponseEntity<ApiResponse<UserV1Dto.PointChargeResponse>> response =
                    testRestTemplate.exchange(CHARGE_USER_POINT_ENDPOINT, HttpMethod.POST,
                            new HttpEntity<>(chargeRequest, headers),
                            new ParameterizedTypeReference<ApiResponse<UserV1Dto.PointChargeResponse>>() {
                            });

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
                    () -> assertThat(response.getBody()).isNotNull()
            );
        }
    }
}