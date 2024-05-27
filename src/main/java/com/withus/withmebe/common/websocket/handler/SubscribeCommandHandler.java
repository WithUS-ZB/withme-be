package com.withus.withmebe.common.websocket.handler;

import static com.withus.withmebe.common.exception.ExceptionCode.AUTHORIZATION_ISSUE;
import static com.withus.withmebe.common.exception.ExceptionCode.INVALID_PATH_FORMAT;
import static com.withus.withmebe.participation.type.Status.CHAT_JOINED;

import com.withus.withmebe.chat.repository.ChatRoomRepository;
import com.withus.withmebe.common.exception.CustomException;
import java.security.Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

@Slf4j
@RequiredArgsConstructor
public class SubscribeCommandHandler implements StompCommandHandler {
  private static final Pattern ROOM_ID_PATTERN = Pattern.compile("/topic/chatroom/(\\d+)");
  private static final String HEADER_SIMP_DESTINATION_KEY = "simpDestination";

  private final ChatRoomRepository chatRoomRepository;

  @Override
  public void handle(StompHeaderAccessor accessor) {
    Principal user = accessor.getUser();
    log.info("[SubscribeCommandHandler][handle]" + user);
    if(user == null){

      throw new CustomException(AUTHORIZATION_ISSUE);
    }
    Long currentMemberId = Long.valueOf(user.getName());
    Long roomId = extractRoomId(accessor.getHeader(HEADER_SIMP_DESTINATION_KEY).toString());

    boolean exists = chatRoomRepository.existsByCurrentMemberIdAndRoomIdAndParticipationStatus(
        currentMemberId, roomId, CHAT_JOINED);

    if (!exists) {
      throw new CustomException(AUTHORIZATION_ISSUE);
    }
  }

  private static Long extractRoomId(String path) {
    log.info("[SubscribeCommandHandler][extractRoomId]" + path);
    Matcher matcher = ROOM_ID_PATTERN.matcher(path);
    if (matcher.matches()) {
      return Long.parseLong(matcher.group(1));
    } else {
      throw new CustomException(INVALID_PATH_FORMAT);
    }
  }
}