package com.example.demo.config;

import com.example.demo.game.service.PointService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
@RequiredArgsConstructor
public class OpenApiPointInterceptor implements HandlerInterceptor {

    private final PointService pointService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // open-api로 시작하는 요청만 포인트 지급
        String uri = request.getRequestURI();
        if (uri.startsWith("/api/open/")) {
            // Swagger에서 넘겨준 학번(userId) 헤더로 받기
            String userId = request.getHeader("x-user-id");

            if (userId != null && !userId.isBlank()) {
                try {
                    pointService.addPointForApiCall(userId, uri);
                } catch (Exception e) {
                    log.error("Open API 포인트 적립에 실패했습니다.", e);
                }
            } else {
                log.debug("사용자 식별 헤더가 없어 Open API 포인트를 적립하지 않았습니다.");
            }
        }

        return true;
    }
}
