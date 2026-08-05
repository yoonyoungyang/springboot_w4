package kr.adapterz.springboot.config;

import kr.adapterz.springboot.entity.ChatRoom;
import kr.adapterz.springboot.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("local")
@Component
@RequiredArgsConstructor
public class ChatDataInitializer implements CommandLineRunner {
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public void run(String... args){
        if(chatRoomRepository.count() == 0) {
            ChatRoom chatRoom = new ChatRoom("임시 채팅방");
            chatRoomRepository.save(chatRoom);
        }

    }

}
