package com.example.booktalk.domain.userReport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.entity.UserRoleType;
import com.example.booktalk.domain.user.repository.UserRepository;
import com.example.booktalk.domain.userreport.dto.request.UserReportCreateReq;
import com.example.booktalk.domain.userreport.dto.response.UserReportCreateRes;
import com.example.booktalk.domain.userreport.dto.response.UserReportListRes;
import com.example.booktalk.domain.userreport.entity.UserReport;
import com.example.booktalk.domain.userreport.exception.NotPermissionSelfReportException;
import com.example.booktalk.domain.userreport.repository.UserReportRepository;
import com.example.booktalk.domain.userreport.service.UserReportService;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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
            .role(UserRoleType.USER)
            .build();
        reportedUser = User.builder()
            .email("email2@gmail.com")
            .password("12345678")
            .randomNickname("nickname2")
            .role(UserRoleType.USER)
            .build();
        ReflectionTestUtils.setField(reportingUser, "id", 1L);
        ReflectionTestUtils.setField(reportedUser, "id", 2L);
    }

    @Nested
    class User_Report_생성_테스트 {

        @Test
        void User_Report_생성_성공() {
            //given
            UserReportCreateReq req = new UserReportCreateReq(reportedUser.getId(), "신고 이유");
            given(userRepository.findUserByIdWithThrow(reportingUser.getId())).willReturn(
                reportingUser);
            given(userRepository.findUserByIdWithNotCache(req.reportedUserId())).willReturn(
                reportedUser);

            //when
            UserReportCreateRes result = userReportService.createUserReport(req,
                reportingUser.getId());

            //then
            assertThat(result.msg()).isEqualTo("신고가 완료되었습니다.");
            verify(userReportRepository, times(1)).save(any(UserReport.class));
        }

        @Test
        void User_Report_자신을_신고할_경우_에러를_반환() {
            //given
            UserReportCreateReq req = new UserReportCreateReq(reportingUser.getId(), "자기 자신 신고");
            given(userRepository.findUserByIdWithThrow(reportingUser.getId())).willReturn(
                reportingUser);
            given(userRepository.findUserByIdWithNotCache(reportingUser.getId())).willReturn(
                reportingUser);

            //when & then
            assertThrows(NotPermissionSelfReportException.class,
                () -> userReportService.createUserReport(req, reportingUser.getId()));

            verify(userReportRepository, never()).save(any());
        }
    }

    @Nested
    class User_Report_조회_테스트 {

        @Test
        void User_Report_조회_성공() {
            //given
            UserReport userReport1 = UserReport.builder()
                .reason("reason1")
                .reportUser(reportingUser)
                .reportedUser(reportedUser)
                .build();
            UserReport userReport2 = UserReport.builder()
                .reason("reason2")
                .reportUser(reportingUser)
                .reportedUser(reportedUser)
                .build();
            LocalDateTime specificTime = LocalDateTime.of(2024, Month.JANUARY, 23, 18, 0, 0);
            ReflectionTestUtils.setField(userReport1, "createdAt", specificTime);
            ReflectionTestUtils.setField(userReport2, "createdAt", specificTime);
            List<UserReport> userReportList = Arrays.asList(userReport1, userReport2);
            given(userReportRepository.findByReportedUserId(reportedUser.getId())).willReturn(
                userReportList);

            //when
            List<UserReportListRes> result = userReportService.getUserReports(reportedUser.getId());

            //then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).reason()).isEqualTo(userReport1.getReason());
            assertThat(result.get(0).createdAt()).isEqualTo(userReport1.getCreatedAt());

            assertThat(result.get(1).reason()).isEqualTo(userReport2.getReason());
            assertThat(result.get(1).createdAt()).isEqualTo(userReport2.getCreatedAt());


        }
    }


}