package com.withus.withmebe.chat.repository;

import com.withus.withmebe.chat.entity.ChatRoom;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.participation.type.Status;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  @Query("SELECT cr FROM ChatRoom cr " +
      "JOIN cr.gathering g " +
      "JOIN Participation p ON p.gathering.id = g.id " +
      "WHERE p.participant.id = :memberId AND p.status = :status")
  Page<ChatRoom> findChatRoomsByStatusAndParticipantId(
      Status status, Long memberId, Pageable pageable);

  @Query("SELECT p.participant FROM Participation p " +
      "JOIN p.gathering g " +
      "JOIN ChatRoom cr ON cr.gathering.id = g.id " +
      "WHERE cr.id = :roomId AND p.status = :status")
  List<Member> findByParticipantOfChatroomByStatusAndRoomId(Status status, Long roomId);

  @Query("SELECT COUNT(cr) > 0 FROM Participation p " +
      "JOIN p.gathering g " +
      "JOIN ChatRoom cr ON cr.gathering.id = g.id " +
      "WHERE p.participant.id = :memberId AND cr.id = :roomId AND p.status = :status")
  boolean existsByCurrentMemberIdAndRoomIdAndParticipationStatus(
      @Param("memberId") Long memberId,
      @Param("roomId") Long roomId,
      @Param("status") Status status);
}
