package kr.adapterz.springboot.controller;

import kr.adapterz.springboot.common.ApiResponse;
import kr.adapterz.springboot.dto.chat.ChatListResponse;
import kr.adapterz.springboot.dto.chat.ChatMessageRequest;
import kr.adapterz.springboot.dto.chat.ChatMessageResponse;
import kr.adapterz.springboot.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;


@RequiredArgsConstructor
@Controller
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;


    @MessageMapping("/chat")
    public void sendMessage(ChatMessageRequest request, Principal principal) {
        System.out.println("ChatController 진입");
        System.out.println("요청 roomId: " + request.getRoomId());
        System.out.println("요청 content: " + request.getContent());
        if (principal == null) {
            throw new IllegalArgumentException("인증 사용자 정보가 없습니다.");
        }
        if (principal.getName() == null || principal.getName().isBlank()) {
            throw new IllegalArgumentException("userId가 비어있습니다.");
        }
        Long loginUserId = Long.valueOf(principal.getName());

        if (loginUserId <= 0) {
            throw new IllegalArgumentException("사용자 ID가 올바르지 않습니다.");
        }

        ChatMessageResponse data = chatService.sendMessage(request, loginUserId);

        messagingTemplate.convertAndSend("/topic/chat/" + request.getRoomId(), data);
        System.out.println("받은 roomId: " + request.getRoomId());
        System.out.println("전송 주소: /topic/chat/" + request.getRoomId());
    }

    @ResponseBody
    @GetMapping("/chat-rooms/{roomId}/messages")
    public ApiResponse<List<ChatListResponse>> messageList(@PathVariable Long roomId) {
        List<ChatListResponse> data = chatService.chatList(roomId);
        return new ApiResponse<>("chat_list_success", data, null);
    }

}
