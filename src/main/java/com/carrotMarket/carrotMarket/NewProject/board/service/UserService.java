package com.carrotMarket.carrotMarket.NewProject.board.service;

import com.carrotMarket.carrotMarket.NewProject.board.entity.User;
import com.carrotMarket.carrotMarket.NewProject.board.mapper.ItemBoardMapper;
import com.carrotMarket.carrotMarket.NewProject.board.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {

        this.userMapper = userMapper;
    }
    public User authenticate(String email, String password) {
        User user = userMapper.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean emailExists(String email) {
        return userMapper.findByEmail(email) != null;
    }
    public void save(User user) {
        userMapper.insertUser(user);
    }


}
