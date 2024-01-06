package com.example.booktalk.domain.comment.controller;

import com.example.booktalk.domain.comment.dto.request.CommentGetListReq;
import com.example.booktalk.domain.comment.dto.request.CommentUpdateReq;
import com.example.booktalk.domain.comment.dto.response.CommentCreateRes;
import com.example.booktalk.domain.comment.dto.response.CommentDeleteRes;
import com.example.booktalk.domain.comment.dto.response.CommentGetListRes;
import com.example.booktalk.domain.comment.dto.response.CommentUpdateRes;
import com.example.booktalk.domain.comment.service.CommentService;
import com.example.booktalk.domain.comment.dto.request.CommentCreateReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentCreateRes> createComment(@RequestBody CommentCreateReq req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(req));
    }

    @GetMapping
    public ResponseEntity<List<CommentGetListRes>> getCommentList(@RequestBody CommentGetListReq req) {
        return ResponseEntity.status(HttpStatus.OK).body((commentService.getCommentList(req.reviewId())));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentUpdateRes> updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateReq req) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(commentId, req));
    }

    @DeleteMapping("/{commentId")
    public ResponseEntity<CommentDeleteRes> deleteComment(@PathVariable Long commentId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.deleteComment(commentId));
    }

}
