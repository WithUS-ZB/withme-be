package com.withus.withmebe.chat.repository;

import com.withus.withmebe.chat.entity.ChatRoom;
import com.withus.withmebe.participation.type.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
  @Query("SELECT cr FROM Participation p " +
      "JOIN p.gathering g " +
      "JOIN ChatRoom cr ON cr.gathering.id = g.id " +
      "WHERE p.status = :status AND p.participant.id = :memberId")
  Page<ChatRoom> findChatRoomsByStatusAndParticipantId(
      Status status, Long memberId, Pageable pageable);
}
