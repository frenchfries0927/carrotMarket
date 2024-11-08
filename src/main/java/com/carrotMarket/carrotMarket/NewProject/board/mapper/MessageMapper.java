package com.carrotMarket.carrotMarket.NewProject.board.mapper;

import com.carrotMarket.carrotMarket.NewProject.board.entity.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageMapper {

    @Insert("INSERT INTO messages (chat_room_id, sender_id, content, sent_at) " +
            "VALUES (#{chatRoomId}, #{senderId}, #{content}, #{sentAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertMessage(Message message);

    @Select("SELECT * FROM messages WHERE id = #{id}")
    Message selectMessageById(Long id);

    @Select("SELECT * FROM messages")
    List<Message> selectAllMessages();

    @Update("UPDATE messages SET chat_room_id = #{chatRoomId}, sender_id = #{senderId}, content = #{content}, sent_at = #{sentAt} WHERE id = #{id}")
    void updateMessage(Message message);

    @Delete("DELETE FROM messages WHERE id = #{id}")
    void deleteMessage(Long id);
}
