package kr.adapterz.springboot.config;

import kr.adapterz.springboot.entity.ChatRoom;
import kr.adapterz.springboot.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("local")
@Component
@RequiredArgsConstructor
public class ChatDataInitializer implements CommandLineRunner {
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public void run(String... args) {
        List<String> roomNames = List.of("용산 아이파크몰 IMAX", "왕십리 IMAX", "여의도 4DX", "코엑스 Dolby Cinema");
        for (String roomName : roomNames) {
            if (!chatRoomRepository.existsByRoomName(roomName)) {
                ChatRoom chatRoom = new ChatRoom(roomName);
                chatRoomRepository.save(chatRoom);
            }
        }
    }
}
