package kr.adapterz.springboot.service;

import kr.adapterz.springboot.dto.chat.ChatRoomResponse;
import kr.adapterz.springboot.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> chatRoomList() {
        return chatRoomRepository.findAllByOrderByRoomIdAsc()
                .stream()
                .map(ChatRoomResponse::new)
                .toList();
    }
}
