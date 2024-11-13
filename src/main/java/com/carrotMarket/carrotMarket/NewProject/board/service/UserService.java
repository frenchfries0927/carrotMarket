package com.carrotMarket.carrotMarket.NewProject.board.service;

import com.carrotMarket.carrotMarket.NewProject.board.entity.User;
import com.carrotMarket.carrotMarket.NewProject.board.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserService {

    private final UserMapper userMapper;

    @Value("${file.upload-dir}") // application.properties의 값을 주입
    private String uploadDir;

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
    private String saveProfileImage(MultipartFile profileImageFile, User user) throws IOException {

        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }


        String fileExtension = profileImageFile.getOriginalFilename()
                .substring(profileImageFile.getOriginalFilename().lastIndexOf("."));
        String fileName = user.getEmail() +fileExtension;

        Path filePath = uploadPath.resolve(fileName);

        // 동일한 이름의 파일이 있으면 삭제
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        // 파일 저장
        profileImageFile.transferTo(filePath.toFile());

        // 파일 저장 (BufferedOutputStream 사용) --프로필 변경 후 list로 리디랙션할 때 변경된 이미지 보여주려고 수정했는데 실패함.
//        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath.toFile()))) {
//            bos.write(profileImageFile.getBytes());
//            bos.flush(); // 버퍼를 플러시하여 파일이 디스크에 즉시 저장되도록 강제
//        }
        return "/profileImages/" + fileName;  // 저장된 파일 경로 반환
    }

    //로그인 시 사용자 위치 정보  update
    public void updateUserLocation(Long userId, Double latitude, Double longitude, String location) {
        userMapper.updateLocation(userId, latitude, longitude, location);
    }

    //프로필 수정시 사용
    public User getLoggedInUser(HttpSession session) {
        // 세션에서 로그인된 사용자 정보 가져오기
        return (User) session.getAttribute("loggedInUser");
    }

    //프로필 생성 및 업데이트
    public void saveOrUpdateUser(User user, MultipartFile profileImageFile) throws IOException {
        // 비밀번호 설정은 필요 시에만 수행
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            // 필요에 따라 비밀번호 암호화 추가 가능
        }

        // 프로필 이미지가 비어 있지 않다면 파일 저장
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            String fileName = saveProfileImage(profileImageFile, user);
            user.setProfileImage(fileName);
        }
        //신규 회원인 경우 기본 권한인 GENERAL로 설정.
        if(user.getUserGroup()==null && user.getId()==null) {
            user.setUserGroup("GENERAL");
        }

        // 사용자 정보 저장 (신규 사용자일 경우 회원가입, 기존 사용자일 경우 업데이트)
        // 데이터베이스에 사용자 저장 또는 업데이트
        User existingUser = userMapper.findByEmail(user.getEmail());
        if (existingUser == null) {
            userMapper.insertUser(user); // 새 사용자 추가
        } else {
            userMapper.updateUser(user); // 기존 사용자 업데이트
        }
    }



}
