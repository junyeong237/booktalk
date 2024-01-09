package com.example.booktalk.domain.user.dto.request;

public record ProfileReqdto(
    String password,
    String newPassword,
    String newPasswordCheck,
    String nickname,
    String location,
    String description,
    String phone
) {

}
