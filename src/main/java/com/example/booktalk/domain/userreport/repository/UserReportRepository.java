package com.example.booktalk.domain.userreport.repository;

import com.example.booktalk.domain.userreport.entity.UserReport;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportRepository extends JpaRepository<UserReport, Long> {

    @CacheEvict(value = "user", key = "#reportedUserId")
    List<UserReport> findByReportedUserId(Long reportedUserId);

}
