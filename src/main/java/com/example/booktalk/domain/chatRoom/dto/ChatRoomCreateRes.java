package com.example.booktalk.domain.chatRoom.dto;


import com.example.booktalk.domain.user.dto.response.UserRes;

public record ChatRoomCreateRes(Long id, UserRes sender, UserRes receiver) {

}
