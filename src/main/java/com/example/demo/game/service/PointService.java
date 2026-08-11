package com.example.demo.game.service;

import com.example.demo.common.dto.PointEventDto;
import com.example.demo.common.sse.LivesSseManager;
import com.example.demo.game.entity.ScoreLog;
import com.example.demo.game.repository.ScoreLogRepository;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PointService {

    private final ScoreLogRepository scoreLogRepository;
    private final UserRepository userRepository;
    private final LivesSseManager livesSseManager; 
    

    /**
     * Open API 호출 시 포인트 지급 로직
     */
    @Transactional
    public int addPointForApiCall(String userId, String uri) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        List<ScoreLog> todayLogs = scoreLogRepository.findByUserAndCreatedAtBetween(user, start, end);
        if (todayLogs.size() >= 3) {
            return 0; // 이미 3회 다 사용했으면 포인트 지급 안 함
        }

        int points = switch (todayLogs.size()) {
            case 0 -> 100; // 1회차
            case 1 -> 130; // 2회차
            case 2 -> 150; // 3회차
            default -> 0;
        };

        // ScoreLog 저장 (히스토리 남김)
        ScoreLog scoreLog = ScoreLog.builder()
                .user(user)
                .source("OPEN_API:" + uri)
                .scoreGiven(points)
                .build();
        scoreLogRepository.save(scoreLog);

        int updated = userRepository.addScore(userId, points);
        if (updated == 0) {
            throw new IllegalStateException("Score update failed for uid=" + userId);
        }

        try {
            int newTotal = user.getScore() + points; // 간단 계산(정확히 하려면 재조회)
            livesSseManager.publishPoints(userId, new PointEventDto(points, newTotal));
            log.debug("Open API 포인트 SSE 전송을 완료했습니다. points={}", points);
        } catch (Exception e) {
            log.warn("Open API 포인트 SSE 전송에 실패했습니다.", e);
        }

        return points;
    }
    public void migrateScores() {
    int updated = userRepository.syncUserScores();
    log.info("사용자 점수 동기화를 완료했습니다. updatedCount={}", updated);
}
}
