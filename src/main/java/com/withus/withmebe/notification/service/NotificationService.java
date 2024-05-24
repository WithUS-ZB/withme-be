package com.withus.withmebe.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.notification.entity.Notification;
import com.withus.withmebe.notification.event.NotificationEvent;
import com.withus.withmebe.notification.event.NotificationSendEvent;
import com.withus.withmebe.notification.repository.NotificationRepository;
import com.withus.withmebe.notification.response.NotificationResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final ApplicationEventPublisher eventPublisher;
  private static final long SSE_TIME_OUT = 1000 * 60 * 60;
  private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
  private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


  public SseEmitter subscribe(long memberId) {
    SseEmitter emitter = new SseEmitter(SSE_TIME_OUT);
    emitters.put(memberId, emitter);
    emitter.onCompletion(() -> emitters.remove(memberId));
    emitter.onTimeout(() -> emitters.remove(memberId));
    emitter.onError((e) -> emitters.remove(memberId));

    try {
      emitter.send(SseEmitter.event().name("connect").data("connected"));
    } catch (IOException e) {
      emitters.remove(memberId);
    }

    return emitter;
  }

  @Async
  public void sendNotifications(NotificationSendEvent notificationSendEvent) {
    long receiver = notificationSendEvent.receiver();
    SseEmitter emitter = emitters.get(receiver);
    if (emitter != null) {
      try {
        emitter.send(OBJECT_MAPPER.writeValueAsString(notificationSendEvent.toResponse()));
      } catch (IOException e) {
        emitters.remove(receiver);
        throw new CustomException(ExceptionCode.FAIL_TO_SEND_NOTIFICATION);
      }
    }
  }

  @EventListener
  public void handleNotificationSendEvent(NotificationSendEvent notificationSendEvent) {
    sendNotifications(notificationSendEvent);
  }

  @Transactional
  @EventListener
  public void createNotificationsFromEvent(NotificationEvent notificationEvent) {
    List<Notification> notifications = createNotifications(notificationEvent);
    for (Notification notification : notifications) {
      eventPublisher.publishEvent(NotificationSendEvent.builder()
          .id(notification.getId())
          .receiver(notification.getReceiver())
          .message(notification.getMessage())
          .notificationType(notification.getNotificationType())
          .createdDttm(notification.getCreatedDttm())
          .build()
      );
    }
  }

  public List<Notification> createNotifications(NotificationEvent notificationEvent) {
    return notificationRepository.saveAll(notificationEvent.toEntities());
  }

  @Transactional(readOnly = true)
  public List<NotificationResponse> readUnreadNotifications(long requesterId) {
    return notificationRepository.findAllByReceiverAndReadDttmIsNull(requesterId)
        .stream().map(Notification::toResponse).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Page<NotificationResponse> readNotifications(long requesterId, Pageable pageable) {
    return notificationRepository.findAllByReceiver(requesterId, pageable)
        .map(Notification::toResponse);
  }

  @Transactional
  public LocalDateTime updateNotificationRead(long requesterId, long notificationId) {

    Notification notification = readNotification(notificationId);
    validateRequesterIsReceiver(requesterId, notification);

    notification.readNotification();
    return notification.getReadDttm();
  }

  public Boolean deleteNotification(long requesterId, long notificationId) {

    Notification notification = readNotification(notificationId);
    validateRequesterIsReceiver(requesterId, notification);

    notificationRepository.delete(notification);
    return true;
  }

  private void validateRequesterIsReceiver(long requesterId, Notification notification) {
    if (!notification.isReceiver(requesterId)) {
      throw new CustomException(ExceptionCode.AUTHORIZATION_ISSUE);
    }
  }

  private Notification readNotification(long notificationId) {
    return notificationRepository.findById(notificationId)
        .orElseThrow(() -> new CustomException(ExceptionCode.ENTITY_NOT_FOUND));
  }
}
