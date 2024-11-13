package com.carrotMarket.carrotMarket.NewProject.board.service;

import com.carrotMarket.carrotMarket.NewProject.board.entity.ItemBoard;
import com.carrotMarket.carrotMarket.NewProject.board.entity.User;
import com.carrotMarket.carrotMarket.NewProject.board.mapper.ItemBoardMapper;
import com.carrotMarket.carrotMarket.NewProject.board.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {

        this.userMapper = userMapper;
    }


    public User getUserById(Long id) {
        return userMapper.selectUserById(id);
    }

    public User authenticate(String email, String password) {
        User user = userMapper.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    public User findByEmail(String email) {

        return userMapper.findByEmail(email);
    }


    public boolean emailExists(String email) {

        return userMapper.findByEmail(email) != null;
    }
    public void save(User user) {
        userMapper.insertUser(user);
    }
    // 카카오 로그인 메소드 추가
    public User kakaoLogin(String email, String username, HttpSession session) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            // 사용자가 없다면 새로 등록
            user = new User();
            user.setEmail(email);
            user.setUsername(username);
            userMapper.insertUser(user); // 새 사용자 등록
        }
        // 세션에 사용자 정보를 저장하여 로그인 상태 유지
        session.setAttribute("loggedInUser", user);
        return user;
    }
    // 프로필 이미지 저장 메소드
    public String saveProfileImage(MultipartFile profileImage) {
        if (profileImage.isEmpty()) {
            return null;
        }

        try {
            String uploadDir = "src/main/resources/static/profileImages/";
            Path uploadPath = Paths.get(uploadDir);

            // 디렉토리가 존재하지 않으면 생성
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 파일 이름을 고유하게 설정 (예: 시간 기반 파일명)
            String fileName = System.currentTimeMillis() + "_" + profileImage.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            profileImage.transferTo(filePath.toFile());

            // 저장된 파일 경로 반환
            return "/profileImages/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //로그인 시 사용자 위치 정보  update
    public void updateUserLocation(Long userId, Double latitude, Double longitude, String location) {
        userMapper.updateLocation(userId, latitude, longitude, location);
    }


}
