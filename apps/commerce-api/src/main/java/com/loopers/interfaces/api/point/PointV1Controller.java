package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointFacade;
import com.loopers.interfaces.api.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PointV1Controller implements PointV1ApiSpec {

    private final PointFacade pointFacade;

    @Override
    @GetMapping("/points")
    public ApiResponse<PointV1Dto.GetPoint.Response> getPoint(
            @RequestHeader(value = "X-USER-ID") Long userId
    ) {
        Long balance = pointFacade.getPoint(userId);
        PointV1Dto.GetPoint.Response response = new PointV1Dto.GetPoint.Response(balance);
        return ApiResponse.success(response);
    }

    @Override
    @PostMapping("/points/charge")
    public ApiResponse<PointV1Dto.ChargePoint.Response> chargePoint(
            @RequestHeader(value = "X-USER-ID") Long userId,
            @RequestBody PointV1Dto.ChargePoint.Request request
    ) {
        Long balance = pointFacade.chargePoint(userId, request.amount());
        PointV1Dto.ChargePoint.Response response = new PointV1Dto.ChargePoint.Response(balance);
        return ApiResponse.success(response);
    }
}
