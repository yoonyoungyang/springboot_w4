package kr.adapterz.springboot.repository;


import kr.adapterz.springboot.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {


    @Query("select c from ChatMessage c join fetch c.sender where c.chatRoom.roomId = :roomId order by c.createdAt asc")
    List<ChatMessage> findAllByChatRoom_RoomId(@Param("roomId") Long roomId);
}
