package com.example.booktalk.domain.admin.service;

import com.example.booktalk.domain.userreport.dto.response.UserReportGetListRes;
import com.example.booktalk.domain.userreport.entity.UserReport;
import com.example.booktalk.domain.userreport.repository.UserReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserReportService {

    private final UserReportRepository userReportRepository;

    public List<UserReportGetListRes> getUserReportList(String sortBy, boolean isAsc) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        List<UserReport> userReportList = userReportRepository.findAll(sort);

        return userReportList.stream()
                .map(userReport -> new UserReportGetListRes( userReport.getId(),
                        userReport.getReportedUser().getId(), userReport.getReason()))
                .toList();
    }
}
