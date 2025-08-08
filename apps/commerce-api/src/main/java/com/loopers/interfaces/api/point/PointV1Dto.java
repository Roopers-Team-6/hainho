package com.loopers.interfaces.api.point;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointV1Dto {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetPoint {
        public record Response(
                Long balance
        ) {
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ChargePoint {
        public record Request(
                Long amount
        ) {
        }

        public record Response(
                Long balance
        ) {
        }
    }

}
