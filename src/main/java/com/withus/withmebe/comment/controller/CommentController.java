package com.withus.withmebe.comment.controller;

import com.withus.withmebe.comment.dto.request.AddCommentRequest;
import com.withus.withmebe.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/add/{gatheringId}")
  public ResponseEntity<?> addComment(@PathVariable long gatheringId,
      @RequestBody AddCommentRequest request) {
    return ResponseEntity.ok(commentService.createComment(gatheringId, request));
  }
}
