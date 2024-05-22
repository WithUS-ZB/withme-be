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

  @Builder
  public ChatRoom(Gathering gathering){
    this.gathering = gathering;
    this.title = gathering.getTitle();
  }

  public ChatRoomDto toDto(){
    return ChatRoomDto.builder()
        .chatId(this.id)
        .title(this.title)
        .gatheringId(this.id)
        .localDateTime(this.gathering.getGatheringDateTime())
        .build();
  }
}