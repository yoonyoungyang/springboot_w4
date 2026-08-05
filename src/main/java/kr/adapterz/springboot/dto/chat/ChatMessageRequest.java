package kr.adapterz.springboot.dto.chat;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter

public class ChatMessageRequest {

    private Long roomId;
    private String content;


}
