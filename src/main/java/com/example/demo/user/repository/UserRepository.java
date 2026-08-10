 package com.example.demo.user.repository;

import com.example.demo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUserId(String userId);

    boolean existsByUserId(String userId);

    Optional<User> findByStudentNumber(String studentNumber);

    // 하트가 1개 이상일 때만 1 감소해 동시 클릭에도 음수를 방지한다.
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE User u SET u.totalLives = u.totalLives - 1 " +
           "WHERE u.userId = :userId AND u.totalLives > 0")
    int consumeOneLife(@Param("userId") String userId);

    @Query("SELECT u.totalLives FROM User u WHERE u.userId = :userId")
    Integer getLives(@Param("userId") String userId);

    // OpenAPI 호출에 따른 점수를 누적한다.
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
           update User u
              set u.score = coalesce(u.score, 0) + :points
            where u.userId = :userId
           """)
    int addScore(@Param("userId") String userId, @Param("points") int points);

    // score_log 합계를 user.score에 동기화한다.
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
           update User u
              set u.score = (
                  select coalesce(sum(s.scoreGiven), 0)
                  from ScoreLog s
                  where s.user = u
              )
           """)
    int syncUserScores();
}
