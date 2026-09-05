package com.example.demo.device.entity;

import java.time.LocalDateTime;

import com.example.demo.school.entity.SchoolEntity;
import com.example.demo.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "pet_input_logs",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_pet_input_device_event",
        columnNames = {"device_id", "event_id"}
    ),
    indexes = {
        @Index(name = "idx_pet_input_user_time", columnList = "user_id,input_time"),
        @Index(name = "idx_pet_input_device_time", columnList = "device_id,input_time")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetInputLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    // 기존 운영 로그는 event_id가 없을 수 있으므로 DB 컬럼은 nullable로 둔다.
    // 신규 입력 API에서는 필수 검증하며, UNIQUE 제약은 신규 이벤트를 보호한다.
    @Column(name = "event_id", length = 80)
    private String eventId;

    @Column(name = "input_count", nullable = false)
    private int inputCount;

    @Column(name = "input_time", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inputTime;

    @ManyToOne
    @JoinColumn(name = "school_id", nullable = false)
    private SchoolEntity school;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable =true)
    private User userId;

    @Column(name = "student_number", length = 20, nullable = true)
    private String studentNumber;
}
