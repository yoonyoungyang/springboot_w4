package kr.adapterz.springboot.controller;

import kr.adapterz.springboot.common.ApiResponse;
import kr.adapterz.springboot.dto.chat.ChatRoomResponse;
import kr.adapterz.springboot.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping("/chat-rooms")
    public ApiResponse<List<ChatRoomResponse>> chatRoomList () {
        List<ChatRoomResponse> data = chatRoomService.chatRoomList();
        return new ApiResponse<>("chat_room_list_success", data, null);
    }

}
