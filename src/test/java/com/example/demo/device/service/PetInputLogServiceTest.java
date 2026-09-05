package com.example.demo.device.service;

import com.example.demo.device.dto.PetInputLogDto;
import com.example.demo.device.dto.PetInputResult;
import com.example.demo.device.entity.Device;
import com.example.demo.device.entity.PetInputLog;
import com.example.demo.device.event.LivesUpdatedEvent;
import com.example.demo.device.repository.DeviceRepository;
import com.example.demo.device.repository.PetInputLogRepository;
import com.example.demo.school.entity.SchoolEntity;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PetInputLogServiceTest {

    private UserRepository userRepository;
    private DeviceRepository deviceRepository;
    private PetInputLogRepository logRepository;
    private ApplicationEventPublisher eventPublisher;
    private PetInputLogService service;
    private PetInputLogDto request;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        deviceRepository = mock(DeviceRepository.class);
        logRepository = mock(PetInputLogRepository.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        service = new PetInputLogService(userRepository, deviceRepository, logRepository, eventPublisher);

        SchoolEntity school = new SchoolEntity();
        school.setId(3L);
        Device device = Device.builder().deviceId(17L).school(school).build();
        User user = User.builder().userId("user-1").studentNumber("20251234").build();

        request = new PetInputLogDto();
        request.setEventId("DEVICE-17-event-1");
        request.setDeviceId(17L);
        request.setStudentNumber("20251234");
        request.setInputCount(1);
        request.setInputTime(LocalDateTime.of(2026, 9, 5, 10, 30));

        when(deviceRepository.findById(17L)).thenReturn(Optional.of(device));
        when(userRepository.findByStudentNumber("20251234")).thenReturn(Optional.of(user));
        when(userRepository.addLives("user-1", 1)).thenReturn(1);
        when(userRepository.getLives("user-1")).thenReturn(4);
        when(logRepository.getTotalCountByUserId("user-1")).thenReturn(11);
    }

    @Test
    void newEventCreditsExactlyOnce() {
        when(logRepository.insertIfAbsent(any(), eq(1), any(), eq(3L), eq(17L),
                eq("user-1"), eq("20251234"))).thenReturn(1);

        PetInputResult result = service.saveInputLog(request);

        assertEquals("PROCESSED", result.status());
        assertEquals(1, result.creditedCount());
        assertEquals(4, result.currentLives());
        verify(userRepository).addLives("user-1", 1);
        verify(eventPublisher).publishEvent(any(LivesUpdatedEvent.class));
    }

    @Test
    void duplicateEventReturnsSuccessWithoutCreditingAgain() {
        PetInputLog existing = PetInputLog.builder()
                .eventId(request.getEventId())
                .studentNumber(request.getStudentNumber())
                .inputCount(request.getInputCount())
                .build();
        when(logRepository.insertIfAbsent(any(), eq(1), any(), eq(3L), eq(17L),
                eq("user-1"), eq("20251234"))).thenReturn(0);
        when(logRepository.findByDevice_DeviceIdAndEventId(17L, request.getEventId()))
                .thenReturn(Optional.of(existing));

        PetInputResult result = service.saveInputLog(request);

        assertEquals("ALREADY_PROCESSED", result.status());
        assertEquals(0, result.creditedCount());
        verify(userRepository, never()).addLives(any(), eq(1));
        verify(eventPublisher, never()).publishEvent(any(LivesUpdatedEvent.class));
    }

    @Test
    void duplicateIdWithDifferentPayloadIsRejected() {
        PetInputLog existing = PetInputLog.builder()
                .eventId(request.getEventId())
                .studentNumber(request.getStudentNumber())
                .inputCount(2)
                .build();
        when(logRepository.insertIfAbsent(any(), eq(1), any(), eq(3L), eq(17L),
                eq("user-1"), eq("20251234"))).thenReturn(0);
        when(logRepository.findByDevice_DeviceIdAndEventId(17L, request.getEventId()))
                .thenReturn(Optional.of(existing));

        assertThrows(IllegalStateException.class, () -> service.saveInputLog(request));
        verify(userRepository, never()).addLives(any(), eq(1));
    }
}
