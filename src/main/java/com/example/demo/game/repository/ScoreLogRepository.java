package com.example.demo.game.repository;

import com.example.demo.game.entity.ScoreLog;
import com.example.demo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ScoreLogRepository extends JpaRepository<ScoreLog, Long> {

    long countByUser_UserIdAndSourceAndCreatedAtBetween(
            String userId,
            String source,
            LocalDateTime start,
            LocalDateTime end
    );

    List<ScoreLog> findByUserAndCreatedAtBetween(
        User user,
        LocalDateTime start,
        LocalDateTime end
    );

    @Query("""
           select s 
             from ScoreLog s
            where s.user.userId = :userId
              and s.createdAt between :start and :end
           """)
    List<ScoreLog> findByUserIdAndDateRange(
            String userId,
            LocalDateTime start,
            LocalDateTime end
    );

    @Query("select coalesce(sum(s.scoreGiven), 0) " +
           "from ScoreLog s " +
           "where s.user.userId = :userId")
    Integer sumScoreByUserId(String userId);
}
