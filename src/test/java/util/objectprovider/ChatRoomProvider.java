package util.objectprovider;

import com.withus.withmebe.chat.entity.ChatRoom;
import com.withus.withmebe.gathering.entity.Gathering;
import org.springframework.test.util.ReflectionTestUtils;

public class ChatRoomProvider {
  private ChatRoomProvider(){}

  public static ChatRoom getStubbedChatRoom(Long chatRoomId, Gathering gathering){
    ChatRoom chatRoom = ChatRoom.builder()
        .gathering(gathering)
        .build();
    ReflectionTestUtils.setField(chatRoom, "id", chatRoomId);
    ReflectionTestUtils.setField(chatRoom, "memberCount", 2L);

    return chatRoom;
  }
}
