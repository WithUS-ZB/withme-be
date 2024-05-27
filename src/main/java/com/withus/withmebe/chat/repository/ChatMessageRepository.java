package com.withus.withmebe.chat.repository;

import com.withus.withmebe.chat.entity.ChatMessage;
import com.withus.withmebe.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

  Page<ChatMessage> findByChatRoom(ChatRoom chatRoom, Pageable pageable);
}
