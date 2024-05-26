package com.withus.withmebe.notification.controller;

import com.withus.withmebe.notification.response.NotificationResponse;
import com.withus.withmebe.notification.service.NotificationService;
import com.withus.withmebe.security.anotation.CurrentMemberId;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter getSseEmitter(@RequestParam("memberid") long memberId) {
    return notificationService.subscribe(memberId);
  }

  @GetMapping("/unread")
  public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(
      @CurrentMemberId long memberId) {
    return ResponseEntity.ok(notificationService.readUnreadNotifications(memberId));
  }

  @GetMapping("/list")
  public ResponseEntity<Page<NotificationResponse>> getNotifications(
      @CurrentMemberId long memberId, @PageableDefault Pageable pageable) {
    return ResponseEntity.ok(notificationService.readNotifications(memberId, pageable));
  }

  @PutMapping("/{notificationid}")
  public ResponseEntity<LocalDateTime> setNotificationRead(
      @CurrentMemberId long memberId, @PathVariable("notificationid") long notificationId) {
    return ResponseEntity.ok(notificationService.updateNotificationRead(memberId, notificationId));
  }

  @DeleteMapping("/{notificationid}")
  public ResponseEntity<Boolean> removeNotification(
      @CurrentMemberId long memberId, @PathVariable("notificationid") long notificationId) {
    return ResponseEntity.ok(notificationService.deleteNotification(memberId, notificationId));
  }
}
