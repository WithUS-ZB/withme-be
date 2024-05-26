package com.withus.withmebe.chat.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.withus.withmebe.chat.dto.response.ChatRoomDto;
import com.withus.withmebe.common.entity.BaseEntity;
import com.withus.withmebe.gathering.entity.Gathering;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom extends BaseEntity {
  @Id
  private Long id;

  @MapsId
  @OneToOne(fetch = LAZY)
  @JoinColumn(name="gathering_id")
  private Gathering gathering;

  private String title;

  private String lastMessageContent;

  private LocalDateTime lastMessageDttm;

  private Long memberCount;

  @Builder
  public ChatRoom(Gathering gathering){
    this.gathering = gathering;
    this.title = gathering.getTitle();
  }

  public ChatRoomDto toDto(){
    return ChatRoomDto.builder()
        .chatroomId(this.id)
        .title(this.title)
        .gatheringId(this.id)
        .gatheringDttm(this.gathering.getGatheringDateTime())
        .lastMessageContent(this.lastMessageContent)
        .lastMessageDttm(this.lastMessageDttm)
        .memberCount(this.memberCount)
        .build();
  }

  public void memberCountUp(){
    this.memberCount = this.memberCount == null ? 1 : this.memberCount+1;
  }

  public void memberCountDown(){
    this.memberCount = this.memberCount == null ? -1 : this.memberCount-1;
  }

  public void updateByMessage(ChatMessage chatMessage){
    this.lastMessageContent = chatMessage.getContent();
    this.lastMessageDttm = chatMessage.getCreatedDttm();
  }
}
