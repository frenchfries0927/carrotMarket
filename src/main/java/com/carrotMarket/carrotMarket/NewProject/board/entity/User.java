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
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String location;
    private Double latitude;
    private Double longitude;
    private String profileImage;
    private Timestamp createdAt;
    private String groupType; // ADMIN or GENERAL
}
