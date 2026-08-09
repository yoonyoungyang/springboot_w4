package kr.adapterz.springboot.dto.chat;

import kr.adapterz.springboot.entity.ChatRoom;
import lombok.Getter;

@Getter
public class ChatRoomResponse {
    private Long roomId;
    private String roomName;

    public ChatRoomResponse(ChatRoom chatRoom) {
        this.roomId = chatRoom.getRoomId();
        this.roomName = chatRoom.getRoomName();
    }
}
