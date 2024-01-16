package com.example.booktalk.domain.userReport.service;

import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import com.example.booktalk.domain.userreport.dto.request.UserReportCreateReq;
import com.example.booktalk.domain.userreport.dto.response.UserReportCreateRes;
import com.example.booktalk.domain.userreport.entity.UserReport;
import com.example.booktalk.domain.userreport.exception.NotFoundReportedUserException;
import com.example.booktalk.domain.userreport.exception.NotPermissionSelfReportException;
import com.example.booktalk.domain.userreport.repository.UserReportRepository;
import com.example.booktalk.domain.userreport.service.UserReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserReportServiceTest {

    @InjectMocks
    UserReportService userReportService;

    @Mock
    UserReportRepository userReportRepository;

    @Mock
    UserRepository userRepository;

    User reportingUser;
    User reportedUser;

    @BeforeEach
    void init() {
        reportingUser = User.builder()
                .email("email@gmail.com")
                .password("12345678")
                .randomNickname("nickname1")
                .build();
        reportedUser = User.builder()
                .email("email2@gmail.com")
                .password("12345678")
                .randomNickname("nickname2")
                .build();
        ReflectionTestUtils.setField(reportingUser, "id", 1L);
        ReflectionTestUtils.setField(reportedUser, "id", 2L);
    }

    @Test
    void User_Report_테스트_성공() {
        //given
        UserReportCreateReq req = new UserReportCreateReq(reportedUser.getId(), "신고 이유");
        given(userRepository.findUserByIdWithThrow(1L)).willReturn(reportingUser);
        given(userRepository.findById(2L)).willReturn(Optional.ofNullable(reportedUser));

        //when
        UserReportCreateRes result = userReportService.createUserReport(req, reportingUser.getId());

        //then
        assertThat(result.msg()).isEqualTo("신고가 완료되었습니다.");
        verify(userReportRepository, times(1)).save(any(UserReport.class));
    }

    @Test
    void User_Report_자신을_신고할_경우_에러를_반환() {
        //given
        UserReportCreateReq req = new UserReportCreateReq(reportingUser.getId(), "자기 자신 신고");
        given(userRepository.findUserByIdWithThrow(1L)).willReturn(reportingUser);
        given(userRepository.findById(1L)).willReturn(Optional.ofNullable(reportingUser));

        //when & then
        assertThrows(NotPermissionSelfReportException.class,
                () -> userReportService.createUserReport(req, reportingUser.getId()));

        verify(userReportRepository, never()).save(any());
    }

}