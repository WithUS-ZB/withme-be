package util.objectprovider;

import static com.withus.withmebe.chat.type.MessageType.CHAT;

import com.withus.withmebe.chat.entity.ChatMessage;
import com.withus.withmebe.chat.entity.ChatRoom;
import com.withus.withmebe.member.entity.Member;
import org.springframework.test.util.ReflectionTestUtils;

public class ChatMessageProvider {
  private final static String CONTENT = "content";

  private ChatMessageProvider() {
  }

  public static ChatMessage getStubbedChatMessage(Long chatMessageId, ChatRoom chatRoom, Member member){
    ChatMessage chatMessage = ChatMessage.builder()
        .chatRoom(chatRoom)
        .chatMember(member)
        .content(CONTENT)
        .type(CHAT)
        .build();
    ReflectionTestUtils.setField(chatMessage, "id", chatMessageId);
    return chatMessage;
  }
}
