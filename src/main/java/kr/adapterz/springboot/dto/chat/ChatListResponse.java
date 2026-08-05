package kr.adapterz.springboot.dto.chat;

import kr.adapterz.springboot.entity.ChatMessage;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatListResponse {
    private Long messageId;
    private String senderNickname;
    private Long senderId;
    private String content;
    private LocalDateTime sendAt;
    private Long roomId;

    public ChatListResponse(ChatMessage chatMessage){
        this.messageId = chatMessage.getMessageId();
        this.senderNickname = chatMessage.getSender().getNickname();
        this.senderId = chatMessage.getSender().getUserId();
        this.content = chatMessage.getContent();
        this.sendAt = chatMessage.getCreatedAt();
        this.roomId = chatMessage.getChatRoom().getRoomId();

    }
}
