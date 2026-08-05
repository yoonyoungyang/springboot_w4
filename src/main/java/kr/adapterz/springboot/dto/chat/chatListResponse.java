package kr.adapterz.springboot.dto.chat;

import kr.adapterz.springboot.entity.ChatMessage;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class chatListResponse {
    private Long meesageId;
    private String senderNickname;
    private String content;
    private LocalDateTime sendAt;
    private Long roomId;

    public chatListResponse(ChatMessage chatMessage){
        this.meesageId = chatMessage.getMessageId();
        this.senderNickname = chatMessage.getSender().getNickname();
        this.content = chatMessage.getContent();
        this.sendAt = chatMessage.getCreatedAt();
        this.roomId = chatMessage.getChatRoom().getRoomId();

    }
}
