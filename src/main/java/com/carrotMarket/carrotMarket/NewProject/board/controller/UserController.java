package com.carrotMarket.carrotMarket.NewProject.board.controller;

import com.carrotMarket.carrotMarket.NewProject.board.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // 회원가입 폼 페이지의 이름 (register.html)
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        // 사용자 정보를 데이터베이스에 저장하는 로직 추가
        // userService.save(user);
        return "redirect:/list"; // 회원가입 완료 후 list.html 페이지로 리디렉션
    }
}
