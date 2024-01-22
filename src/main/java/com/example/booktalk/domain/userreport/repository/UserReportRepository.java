package com.example.booktalk.domain.userreport.repository;

import com.example.booktalk.domain.userreport.entity.UserReport;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {

    List<UserReport> findByReportedUserId(Long reportedUserId);
    
}
