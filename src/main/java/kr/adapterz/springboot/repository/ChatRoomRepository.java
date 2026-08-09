package kr.adapterz.springboot.repository;

import kr.adapterz.springboot.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    boolean existsByRoomName(String roomName);

    List<ChatRoom> findAllByOrderByRoomIdAsc();
}
