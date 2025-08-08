package com.loopers.interfaces.api.point;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.user.UserV1Dto;
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
class PointV1ControllerE2ETest {
    private static final String REGISTER_USER_ENDPOINT = "/api/v1/users";
    private static final String GET_POINT_ENDPOINT = "/api/v1/points";

    private final TestRestTemplate testRestTemplate;
    private final DatabaseCleanUp databaseCleanUp;

    @Autowired
    public PointV1ControllerE2ETest(
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
            ResponseEntity<ApiResponse<PointV1Dto.GetPoint.Response>> response =
                    testRestTemplate.exchange(GET_POINT_ENDPOINT, HttpMethod.GET, new HttpEntity<>(headers),
                            new ParameterizedTypeReference<ApiResponse<PointV1Dto.GetPoint.Response>>() {
                            });
            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(response.getBody()).isNotNull(),
                    () -> assertThat(response.getBody().data()).isNotNull(),
                    () -> assertThat(response.getBody().data().balance()).isZero()
            );
        }

        @Test
        @DisplayName("X-USER-ID 헤더가 없을 경우, 400 Bad Request 응답을 반환한다.")
        void getUserPointWithoutHeader() {
            // act
            ResponseEntity<ApiResponse<PointV1Dto.GetPoint.Response>> response =
                    testRestTemplate.exchange(GET_POINT_ENDPOINT, HttpMethod.GET, null,
                            new ParameterizedTypeReference<ApiResponse<PointV1Dto.GetPoint.Response>>() {
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

            ResponseEntity<ApiResponse<PointV1Dto.GetPoint.Response>> getPointResponse =
                    testRestTemplate.exchange(GET_POINT_ENDPOINT, HttpMethod.GET, new HttpEntity<>(headers),
                            new ParameterizedTypeReference<ApiResponse<PointV1Dto.GetPoint.Response>>() {
                            });
            Long initialPoint = getPointResponse.getBody().data().balance();
            Long chargingPoint = 1000L;

            PointV1Dto.ChargePoint.Request chargeRequest = new PointV1Dto.ChargePoint.Request(chargingPoint);

            // act
            ResponseEntity<ApiResponse<PointV1Dto.ChargePoint.Response>> response =
                    testRestTemplate.exchange(CHARGE_USER_POINT_ENDPOINT, HttpMethod.POST,
                            new HttpEntity<>(chargeRequest, headers),
                            new ParameterizedTypeReference<ApiResponse<PointV1Dto.ChargePoint.Response>>() {
                            });

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(response.getBody()).isNotNull(),
                    () -> assertThat(response.getBody().data()).isNotNull(),
                    () -> assertThat(response.getBody().data().balance()).isEqualTo(initialPoint + chargingPoint)
            );
        }

        @Test
        @DisplayName("존재하지 않는 UserId로 포인트 충전 시도 시, 404 Not Found 응답을 반환한다.")
        void chargeUserPointNotFound() {
            // arrange
            Long userId = 1L;
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-USER-ID", userId.toString());

            Long chargingPoint = 1000L;
            PointV1Dto.ChargePoint.Request chargeRequest = new PointV1Dto.ChargePoint.Request(chargingPoint);

            // act
            ResponseEntity<ApiResponse<PointV1Dto.ChargePoint.Response>> response =
                    testRestTemplate.exchange(CHARGE_USER_POINT_ENDPOINT, HttpMethod.POST,
                            new HttpEntity<>(chargeRequest, headers),
                            new ParameterizedTypeReference<ApiResponse<PointV1Dto.ChargePoint.Response>>() {
                            });

            // assert
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
                    () -> assertThat(response.getBody()).isNotNull()
            );
        }
    }
}