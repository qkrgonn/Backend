package com.example.demo.device.service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.user.dto.LivesDto;
import com.example.demo.device.dto.PetInputLogDto;
import com.example.demo.device.dto.PetInputResult;
import com.example.demo.device.entity.Device;
import com.example.demo.device.entity.PetInputLog;
import com.example.demo.device.event.LivesUpdatedEvent;
import com.example.demo.device.repository.DeviceRepository;
import com.example.demo.device.repository.PetInputLogRepository;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PetInputLogService {

    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;
    private final PetInputLogRepository petInputLogRepository;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public PetInputResult saveInputLog(PetInputLogDto dto) {
        validate(dto);

        // user는 찾되, 없어도 그냥 null 허용
        User user = null;
        if (dto.getStudentNumber() != null && !dto.getStudentNumber().isBlank()) {
            user = userRepository.findByStudentNumber(dto.getStudentNumber()).orElse(null);
        }

        Device device = deviceRepository.findById(dto.getDeviceId()).orElse(null);
        if (device == null) {
            throw new IllegalArgumentException("등록되지 않은 디바이스입니다");
        }

        var school = device.getSchool();
        LocalDateTime inputTime = dto.getInputTime() != null ? dto.getInputTime() : LocalDateTime.now();
        String userId = user != null ? user.getUserId() : null;

        int inserted = petInputLogRepository.insertIfAbsent(
                dto.getEventId(),
                dto.getInputCount(),
                inputTime,
                school.getId(),
                device.getDeviceId(),
                userId,
                dto.getStudentNumber());

        if (inserted == 0) {
            PetInputLog existing = petInputLogRepository
                    .findByDevice_DeviceIdAndEventId(dto.getDeviceId(), dto.getEventId())
                    .orElseThrow(() -> new IllegalStateException("중복 이벤트 조회에 실패했습니다"));
            ensureSameEvent(existing, dto);
            return new PetInputResult(dto.getEventId(), "ALREADY_PROCESSED", 0,
                    userId != null ? userRepository.getLives(userId) : null,
                    userId != null ? safeTotal(userId) : null);
        }

        if (user != null) {
            int updated = userRepository.addLives(user.getUserId(), dto.getInputCount());
            if (updated != 1) {
                throw new IllegalStateException("사용자 적립 갱신에 실패했습니다");
            }

            int currentLives = userRepository.getLives(user.getUserId());
            int totalRecycleCount = safeTotal(user.getUserId());

            // 리스너가 DB 커밋에 성공한 뒤에만 SSE를 전송한다.
            eventPublisher.publishEvent(new LivesUpdatedEvent(
                user.getUserId(),
                new LivesDto(
                    user.getUserId(),
                    currentLives,
                    totalRecycleCount,
                    LocalDateTime.now(),
                    dto.getInputCount() )
            ));

            return new PetInputResult(dto.getEventId(), "PROCESSED", dto.getInputCount(),
                    currentLives, totalRecycleCount);
        }

        return new PetInputResult(dto.getEventId(), "PROCESSED", 0, null, null);
    }

    private void validate(PetInputLogDto dto) {
        if (dto.getEventId() == null || dto.getEventId().isBlank() || dto.getEventId().length() > 80) {
            throw new IllegalArgumentException("eventId는 1~80자의 필수 값입니다");
        }
        if (dto.getDeviceId() == null) {
            throw new IllegalArgumentException("deviceId는 필수 값입니다");
        }
        if (dto.getInputCount() <= 0) {
            throw new IllegalArgumentException("정상 PET이 아니므로 저장하지 않음");
        }
        if (dto.getStudentNumber() != null && dto.getStudentNumber().length() > 20) {
            throw new IllegalArgumentException("studentNumber는 최대 20자입니다");
        }
    }

    private void ensureSameEvent(PetInputLog existing, PetInputLogDto dto) {
        String existingStudentNumber = existing.getStudentNumber();
        boolean sameStudent = java.util.Objects.equals(existingStudentNumber, dto.getStudentNumber());
        if (!sameStudent || existing.getInputCount() != dto.getInputCount()) {
            throw new IllegalStateException("같은 eventId에 서로 다른 적립 데이터가 전달되었습니다");
        }
    }

    private int safeTotal(String userId) {
        Integer total = petInputLogRepository.getTotalCountByUserId(userId);
        return total != null ? total : 0;
    }

    public List<PetInputLogDto> getLogsByUserId(String userId) {
        long start = System.currentTimeMillis();

        // userId가 User 객체일 경우는 아래처럼 언더스코어(_) 사용
        List<PetInputLog> logs = petInputLogRepository.findTop50ByUserId_UserIdOrderByInputTimeDesc(userId);

        long end = System.currentTimeMillis();
        log.debug("사용자 투입 로그 조회를 완료했습니다. durationMs={}", end - start);

        return logs.stream().map(log -> {
            PetInputLogDto dto = new PetInputLogDto();
            dto.setEventId(log.getEventId());
            dto.setUserId(log.getUserId() != null ? log.getUserId().getUserId() : null);
            dto.setDeviceId(log.getDevice().getDeviceId());
            dto.setInputCount(log.getInputCount());
            dto.setInputTime(log.getInputTime());
            return dto;
        }).collect(Collectors.toList());
    }
}
