package com.withus.withmebe.chat.entity;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.withus.withmebe.chat.dto.ChatMessageDto;
import com.withus.withmebe.chat.type.MessageType;
import com.withus.withmebe.common.entity.BaseEntity;
import com.withus.withmebe.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatMessage extends BaseEntity {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "chat_message_history_id")
  private Long id;


  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "chatroom_id")
  ChatRoom chatRoom;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "chat_member_id")
  Member chatMember;

  @Column(nullable = false)
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private MessageType type;

  @Builder
  public ChatMessage(ChatRoom chatRoom, Member chatMember, String content, MessageType type) {
    this.chatRoom = chatRoom;
    this.chatMember = chatMember;
    this.content = content;
    this.type = type;
  }

  public ChatMessageDto toChatMessageDto() {
    return ChatMessageDto.builder()
        .chatId(this.id)
        .content(this.content)
        .memberId(this.chatMember.getId())
        .profileImg(this.chatMember.getProfileImg())
        .nickName(this.chatMember.getNickName())
        .messageType(this.type)
        .chatDateTime(this.getCreatedDttm())
        .build();
  }
}
