package kr.adapterz.springboot.dto.chat;
import kr.adapterz.springboot.entity.ChatMessage;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter

public class ChatMessageResponse {

    private Long roomId;
    private Long messageId;
    private Long senderId;
    private String senderNickname;
    private String content;
    private LocalDateTime sendAt;

    public ChatMessageResponse(ChatMessage chatMessage) {
        this.roomId = chatMessage.getChatRoom().getRoomId();
        this.messageId = chatMessage.getMessageId();
        this.senderId = chatMessage.getSender().getUserId();
        this.senderNickname = chatMessage.getSender().getNickname();
        this.content = chatMessage.getContent();
        this.sendAt = chatMessage.getCreatedAt();
    }
}
