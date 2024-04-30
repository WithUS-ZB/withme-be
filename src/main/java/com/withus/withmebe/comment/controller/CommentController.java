package com.withus.withmebe.comment.controller;

import com.withus.withmebe.comment.dto.request.AddCommentRequest;
import com.withus.withmebe.comment.dto.request.SetCommentRequest;
import com.withus.withmebe.comment.dto.response.CommentResponse;
import com.withus.withmebe.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/add/{gatheringId}")
  public ResponseEntity<CommentResponse> addComment(@PathVariable long gatheringId,
      @RequestBody AddCommentRequest request) {
    return ResponseEntity.ok(commentService.createComment(gatheringId, request));
  }

  @GetMapping("/list/{gatheringId}")
  public ResponseEntity<Page<CommentResponse>> getComments(@PathVariable long gatheringId,
      @PageableDefault(size = 10, sort = "createdDttm") Pageable pageble) {
    return ResponseEntity.ok(commentService.readComments(gatheringId, pageble));
  }

  @PutMapping("/{commentId}")
  public ResponseEntity<CommentResponse> setComment(@PathVariable long commentId,
      @RequestBody SetCommentRequest request) {
    return ResponseEntity.ok(commentService.updateComment(commentId, request));
  }

  @DeleteMapping("/{commentId}")
  public ResponseEntity<CommentResponse> removeComment(@PathVariable long commentId) {
    return ResponseEntity.ok(commentService.deleteComment(commentId));
  }
}
