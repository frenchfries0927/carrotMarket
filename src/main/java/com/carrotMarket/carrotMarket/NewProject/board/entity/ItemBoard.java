package com.carrotMarket.carrotMarket.NewProject.board.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemBoard {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String category; // BOOKS, CLOTHING, ELECTRONICS, etc.
    private Timestamp createdAt;
    private Long userId;
    private String itemImage;
    private String status; // RESERVED, SALE, SOLD
}
