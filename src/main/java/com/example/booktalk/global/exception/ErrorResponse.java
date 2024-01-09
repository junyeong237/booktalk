package com.example.booktalk.global.exception;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private int status;

    private List<String> messages;

    @Builder
    public ErrorResponse(int status, List<String> messages) {
        this.status = status;
        this.messages = (messages != null) ? messages : new ArrayList<>();

    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

}
