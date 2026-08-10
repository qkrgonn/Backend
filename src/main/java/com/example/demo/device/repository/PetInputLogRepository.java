package com.example.demo.device.repository;

import com.example.demo.device.entity.PetInputLog;
import com.example.demo.openapi.dto.StudentsRankingDto;
import com.example.demo.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PetInputLogRepository extends JpaRepository<PetInputLog, Long> {

    List<PetInputLog> findTop50ByUserId_UserIdOrderByInputTimeDesc(String userId);

    int countByUserId(User user);

    @Query("SELECT SUM(p.inputCount) FROM PetInputLog p WHERE p.userId.userId = :userId")
    Integer getTotalCountByUserId(@Param("userId") String userId);

    @Query("SELECT SUM(p.inputCount) FROM PetInputLog p " +
           "WHERE p.userId.userId = :userId AND p.inputTime >= :startDate")
    Integer getTotalCountByUserIdAndDate(@Param("userId") String userId,
                                         @Param("startDate") LocalDateTime startDate);

    @Query("SELECT SUM(p.inputCount) FROM PetInputLog p WHERE p.school.id = :schoolId")
    Integer getTotalCountBySchoolId(@Param("schoolId") Long schoolId);

    @Query("SELECT FUNCTION('DATE', p.inputTime), SUM(p.inputCount) " +
           "FROM PetInputLog p WHERE p.school.id = :schoolId " +
           "GROUP BY FUNCTION('DATE', p.inputTime) " +
           "ORDER BY FUNCTION('DATE', p.inputTime)")
    List<Object[]> getDailyStatsBySchoolId(@Param("schoolId") Long schoolId);

  @Query("""
    SELECT new com.example.demo.openapi.dto.StudentsRankingDto(
        u.userId,
        u.name,
        SUM(p.inputCount)
    )
    FROM PetInputLog p
    JOIN p.userId u
    WHERE p.school.id = :schoolId
    GROUP BY u.userId, u.name
    ORDER BY SUM(p.inputCount) DESC
""")
List<StudentsRankingDto> getStudentsRanking(@Param("schoolId") Long schoolId);
}
