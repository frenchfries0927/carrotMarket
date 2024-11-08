package com.carrotMarket.carrotMarket.NewProject.board.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Long id;
    private Long chatRoomId; // FK to ChatRoom
    private Long senderId;   // FK to User
    private String content;
    private Timestamp sentAt;
}
