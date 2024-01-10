package com.example.booktalk.domain.userreport.repository;

import com.example.booktalk.domain.userreport.entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {
}
