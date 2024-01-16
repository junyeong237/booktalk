package com.example.booktalk.domain.user.dto.request;

public record UserPWUpdateReq(
    String password,
    String newPassword,
    String newPasswordCheck
) {

}
