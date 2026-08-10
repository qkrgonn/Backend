package com.example.demo.device.repository;

import com.example.demo.device.entity.Device;
import com.example.demo.school.entity.SchoolEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findBySchool(SchoolEntity school);

    List<Device> findBySchool_Id(Long schoolId);
}
