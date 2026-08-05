package kr.adapterz.springboot.service;

import kr.adapterz.springboot.dto.PostListResponse;
import kr.adapterz.springboot.dto.chat.ChatListResponse;
import kr.adapterz.springboot.dto.chat.ChatMessageRequest;
import kr.adapterz.springboot.dto.chat.ChatMessageResponse;
import kr.adapterz.springboot.entity.ChatMessage;
import kr.adapterz.springboot.entity.ChatRoom;
import kr.adapterz.springboot.entity.User;
import kr.adapterz.springboot.repository.ChatMessageRepository;
import kr.adapterz.springboot.repository.ChatRoomRepository;
import kr.adapterz.springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageResponse sendMessage (ChatMessageRequest request, Long loginUserId) {
        System.out.println("ChatService 진입");
        System.out.println("loginUserId: " + loginUserId);
        System.out.println("roomId: " + request.getRoomId());

        User sender = userRepository.findUserByUserIdAndDeletedAtIsNull(loginUserId).orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
        ChatRoom chatRoom = chatRoomRepository.findById(request.getRoomId()).orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));
        System.out.println("사용자 조회 성공: " + sender.getNickname());
        System.out.println("채팅방 조회 성공: " + chatRoom.getRoomName());

        if(request.getContent()==null || request.getContent().isBlank()) {
            throw new IllegalArgumentException("내용이 비어있습니다.");
        }
        ChatMessage chatMessage =  ChatMessage.builder()
                .sender(sender)
                .chatRoom(chatRoom)
                .content(request.getContent())
                .build();

        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);
        System.out.println("저장된 messageId: " + savedChatMessage.getMessageId());
        return new ChatMessageResponse(savedChatMessage);



    }
    public List<ChatListResponse> chatList(Long roomId) {
        boolean isChatRoomExists = chatRoomRepository.existsById(roomId);
        if (!isChatRoomExists) {
            throw new RuntimeException("채팅방이 없습니다.");
        }
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoom_RoomId(roomId);
        return chatMessages
                .stream()
                .map(ChatListResponse::new)
                .toList();
    }

}
