package com.carrotMarket.carrotMarket.NewProject.board.entity;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
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
    private String userGroup; // ADMIN or GENERAL
}
