package com.example.demo.school.repository;

import com.example.demo.school.entity.SchoolEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<SchoolEntity, Long> {
    
    List<SchoolEntity> findBySchoolNameContaining(String keyword);

    Optional<SchoolEntity> findBySchoolName(String name);

    boolean existsBySchoolName(String schoolName);

    List<SchoolEntity> findByAdminRegion(String adminRegion);
    List<SchoolEntity> findByRegion(String region);

    Page<SchoolEntity> findBySchoolNameContainingIgnoreCase(String keyword, Pageable pageable);

    Page<SchoolEntity> findByRegionAndSchoolNameContainingIgnoreCase(
            String region, String keyword, Pageable pageable);

    Page<SchoolEntity> findByAdminRegionAndSchoolNameContainingIgnoreCase(
            String adminRegion, String keyword, Pageable pageable);

    Page<SchoolEntity> findByRegion(String region, Pageable pageable);
    Page<SchoolEntity> findByAdminRegion(String adminRegion, Pageable pageable);
}
