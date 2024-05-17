package com.withus.withmebe.comment.controller;

import com.withus.withmebe.comment.dto.request.AddCommentRequest;
import com.withus.withmebe.comment.dto.request.SetCommentRequest;
import com.withus.withmebe.comment.dto.response.CommentResponse;
import com.withus.withmebe.comment.service.CommentService;
import com.withus.withmebe.security.anotation.CurrentMemberId;
import com.withus.withmebe.security.domain.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/add")
  public ResponseEntity<CommentResponse> addComment(
      @CurrentMemberId long currentMemberId,
      @RequestParam(value = "gatheringid") long gatheringId,
      @Valid @RequestBody AddCommentRequest request) {
    return ResponseEntity.ok(
        commentService.createComment(currentMemberId, gatheringId, request));
  }

  @GetMapping("/list/{gatheringId}")
  public ResponseEntity<Page<CommentResponse>> getComments(@PathVariable long gatheringId,
      @PageableDefault(sort = "createdDttm", direction = Direction.DESC) Pageable pageble) {
    return ResponseEntity.ok(commentService.readComments(gatheringId, pageble));
  }

  @PutMapping("/{commentId}")
  public ResponseEntity<CommentResponse> setComment(
      @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable long commentId,
      @Valid @RequestBody SetCommentRequest request) {
    return ResponseEntity.ok(
        commentService.updateComment(customUserDetails.getMemberId(), commentId, request));
  }

  @DeleteMapping("/{commentId}")
  public ResponseEntity<CommentResponse> removeComment(
      @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable long commentId) {
    return ResponseEntity.ok(
        commentService.deleteComment(customUserDetails.getMemberId(), commentId));
  }
}
