package com.withus.withmebe.notification.entity;

import com.withus.withmebe.common.entity.BaseEntity;
import com.withus.withmebe.notification.event.NotificationSendEvent;
import com.withus.withmebe.notification.response.NotificationResponse;
import com.withus.withmebe.notification.type.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor
@Getter
@Where(clause = "deleted_dttm is null")
@SQLDelete(sql = "UPDATE notification SET deleted_dttm = NOW() WHERE notification_id = ?")
public class Notification extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "notification_id")
  private Long id;

  @Column(nullable = false, name = "member_id")
  private Long receiver;

  @Column(nullable = false)
  private String message;

  @Column(nullable = false)
  private NotificationType notificationType;

  private LocalDateTime readDttm;

  @Builder
  public Notification(Long memberId, String message, NotificationType notificationType) {
    this.receiver = memberId;
    this.message = message;
    this.notificationType = notificationType;
  }

  public NotificationSendEvent toSendEvent() {
    return NotificationSendEvent.builder()
        .id(this.id)
        .receiver(this.receiver)
        .message(this.message)
        .notificationType(this.notificationType)
        .createdDttm(this.getCreatedDttm())
        .build();
  }

  public NotificationResponse toResponse() {
    return NotificationResponse.builder()
        .id(this.id)
        .message(this.message)
        .notificationType(this.notificationType)
        .readDttm((this.readDttm != null) ? this.readDttm.toString():null)
        .createdDttm(this.getCreatedDttm().toString())
        .build();
  }

  public boolean isReceiver(long memberId) {
    return receiver == memberId;
  }

  public void readNotification() {
    this.readDttm = LocalDateTime.now();
  }
}
