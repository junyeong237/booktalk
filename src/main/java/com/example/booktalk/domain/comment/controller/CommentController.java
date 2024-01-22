package com.example.booktalk.domain.comment.controller;

import com.example.booktalk.domain.comment.dto.request.CommentGetListReq;
import com.example.booktalk.domain.comment.dto.request.CommentUpdateReq;
import com.example.booktalk.domain.comment.dto.response.CommentCreateRes;
import com.example.booktalk.domain.comment.dto.response.CommentDeleteRes;
import com.example.booktalk.domain.comment.dto.response.CommentGetListRes;
import com.example.booktalk.domain.comment.dto.response.CommentUpdateRes;
import com.example.booktalk.domain.comment.service.CommentService;
import com.example.booktalk.domain.comment.dto.request.CommentCreateReq;
import com.example.booktalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentCreateRes> createComment(
            @RequestBody CommentCreateReq req,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(req, userDetails.getUser().getId()));
    }

    @GetMapping
    public ResponseEntity<List<CommentGetListRes>> getCommentList(
            @RequestBody CommentGetListReq req
    ) {
        return ResponseEntity.status(HttpStatus.OK).body((commentService.getCommentList(req.reviewId())));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentUpdateRes> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentUpdateReq req,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(commentId, req, userDetails.getUser().getId()));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentDeleteRes> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.deleteComment(commentId, userDetails.getUser().getId()));
    }

}
