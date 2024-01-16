package com.example.booktalk.domain.userreport.service;

import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import com.example.booktalk.domain.userreport.dto.request.UserReportCreateReq;
import com.example.booktalk.domain.userreport.dto.response.UserReportCreateRes;
import com.example.booktalk.domain.userreport.entity.UserReport;
import com.example.booktalk.domain.userreport.exception.NotFoundReportedUserException;
import com.example.booktalk.domain.userreport.exception.NotPermissionSelfReportException;
import com.example.booktalk.domain.userreport.exception.UserReportErrorCode;
import com.example.booktalk.domain.userreport.repository.UserReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserReportService {

    private final UserReportRepository userReportRepository;
    private final UserRepository userRepository;

    public UserReportCreateRes createUserReport(UserReportCreateReq req, Long userId) {

        User user = userRepository.findUserByIdWithThrow(userId);
        User reportedUser = findReportedUser(req.reportedUserId());

        if(user.getId().equals(reportedUser.getId())) {
            throw new NotPermissionSelfReportException(UserReportErrorCode.NOT_PERMISSION_SELF_REPORT);
        }

        UserReport userReport = UserReport.builder()
                .reason(req.reason())
                .reportedUser(reportedUser)
                .reportUser(user)
                .build();
        reportedUser.increaseReportCount();
        userReportRepository.save(userReport);

        return new UserReportCreateRes("신고가 완료되었습니다.");
    }

    private User findReportedUser(Long reportedUserId) {
        return userRepository.findById(reportedUserId)
                .orElseThrow(() -> new NotFoundReportedUserException(UserReportErrorCode.NOT_FOUND_REPORTED_USER));
    }

}
