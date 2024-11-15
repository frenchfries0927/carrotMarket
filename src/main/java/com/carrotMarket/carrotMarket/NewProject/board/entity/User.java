package com.carrotMarket.carrotMarket.NewProject.board.entity;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class User implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String location;
    private Double latitude;
    private Double longitude;
    private String userGroup;
    private String profileImage;

    // 생성일 필드 추가
    private Timestamp createdAt;

    // 생성일 필드에 대한 Getter와 Setter가 자동 생성됩니다 (lombok @Data 어노테이션 사용).
}
