package com.carrotMarket.carrotMarket.NewProject.board.mapper;

import com.carrotMarket.carrotMarket.NewProject.board.entity.ChatRoom;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ChatRoomMapper {

    // 채팅방 생성 메서드
    @Insert("INSERT INTO chat_room (item_id, buyer_id, seller_id, created_at) " +
            "VALUES (#{itemId}, #{buyerId}, #{sellerId}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertChatRoom(ChatRoom chatRoom);

    // 모든 채팅방 가져오기
    @Select("SELECT * FROM chat_room")
    List<ChatRoom> getAllChatRooms();

    // 특정 채팅방 가져오기 (ID로 검색)
    @Select("SELECT * FROM chat_room WHERE id = #{id}")
    ChatRoom getChatRoomById(Long id);
}
