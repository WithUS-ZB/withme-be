package com.withus.withmebe.notification.controller;

import com.withus.withmebe.notification.response.NotificationResponse;
import com.withus.withmebe.notification.service.NotificationService;
import com.withus.withmebe.security.anotation.CurrentMemberId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter getSseEmitter(@CurrentMemberId long memberId) {
    return notificationService.subscribe(memberId);
  }

  @GetMapping("/unread")
  public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(
      @CurrentMemberId long memberId) {
    return ResponseEntity.ok(notificationService.readUnreadNotifications(memberId));
  }

}
