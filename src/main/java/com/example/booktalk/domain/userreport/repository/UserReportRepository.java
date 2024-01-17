package com.example.booktalk.domain.userreport.repository;

import com.example.booktalk.domain.userreport.entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {
    List<UserReport> findByReportedUserId(Long reportedUserId);
}
