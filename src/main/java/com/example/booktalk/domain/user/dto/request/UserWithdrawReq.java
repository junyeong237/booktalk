package com.example.booktalk.domain.user.dto.request;

public record UserWithdrawReq(
    String password,
    String passwordCheck
) {

}
