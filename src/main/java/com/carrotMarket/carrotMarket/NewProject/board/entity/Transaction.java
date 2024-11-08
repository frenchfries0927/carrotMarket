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
public class Transaction {
    private Long id;
    private Long itemId;  // FK to ItemBoard
    private Long buyerId; // FK to User
    private String status; // CANCELLED, COMPLETED, PENDING
    private Timestamp transactionDate;
}
