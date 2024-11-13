package com.carrotMarket.carrotMarket.NewProject.board.service;

import com.carrotMarket.carrotMarket.NewProject.board.entity.ItemBoard;
import com.carrotMarket.carrotMarket.NewProject.board.entity.User;
import com.carrotMarket.carrotMarket.NewProject.board.mapper.ItemBoardMapper;
import com.carrotMarket.carrotMarket.NewProject.board.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    //로그인 시 사용자 위치 정보  update
    public void updateUserLocation(Long userId, Double latitude, Double longitude, String location) {
        userMapper.updateLocation(userId, latitude, longitude, location);
    }


}
