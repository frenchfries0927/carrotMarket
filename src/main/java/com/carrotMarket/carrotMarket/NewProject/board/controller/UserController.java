package com.carrotMarket.carrotMarket.NewProject.board.controller;

import com.carrotMarket.carrotMarket.NewProject.board.entity.User;
import com.carrotMarket.carrotMarket.NewProject.board.service.ItemBoardService;
import com.carrotMarket.carrotMarket.NewProject.board.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //회원가입
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // 회원가입 폼 페이지의 이름 (register.html)
    }

    // 회원가입 완료
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, HttpSession session) {
        // 사용자 정보를 데이터베이스에 저장하는 로직 추가
        user.setUserGroup("GENERAL");
        userService.save(user);

        // 회원 가입 후 자동 로그인
        session.setAttribute("loggedInUser", user); // 세션에 사용자 정보 저장

        return "redirect:/board/list"; // 회원가입 완료 후 list.html 페이지로 리디렉션
    }

    //로그인
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        User user = userService.authenticate(email, password);
        if (user != null) {
            session.setAttribute("loggedInUser", user); // 세션에 사용자 정보 저장
            return "redirect:/board/list";
        } else {
           // return "redirect:/?loginError=true";
            return "redirect:/board/list?loginError=true"; // 로그인 실패 시 loginError 매개변수 추가
        }
    }
    //로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/board/list";
    }
    
    //이메일 중복가입 체크
    @PostMapping("/check-email")
    @ResponseBody
    public Map<String, Boolean> checkEmail(@RequestParam("email") String email) {
        boolean exists = userService.emailExists(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return response;
    }
    @PostMapping("/kakaoLogin")
    @ResponseBody
    public String kakaoLogin(@RequestParam String email, @RequestParam String username, HttpSession session) {
        User user = userService.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setUsername(username);
            userService.save(user);
        }
        session.setAttribute("loggedInUser", user);
        return "redirect:/board/list";
    }




//    @PostMapping("/register")
//    public String registerUser(@ModelAttribute("user") User user,
//                               @RequestParam("profileImage") MultipartFile profileImage,
//                               BindingResult bindingResult,
//                               HttpSession session) {
//        if (bindingResult.hasErrors()) {
//            return "register";
//        }
//
//        // 프로필 이미지 파일 처리
//        if (!profileImage.isEmpty()) {
//            try {
//                // static/profileImages 폴더에 저장할 경로 설정
//                String uploadDir = "src/main/resources/static/profileImages/";
//                Path uploadPath = Paths.get(uploadDir);
//
//                if (!Files.exists(uploadPath)) {
//                    Files.createDirectories(uploadPath);
//                }
//
//                // 파일 이름을 고유하게 설정 (예: 시간 기반 파일명)
//                String fileName = System.currentTimeMillis() + "_" + profileImage.getOriginalFilename();
//                Path filePath = uploadPath.resolve(fileName);
//                profileImage.transferTo(filePath.toFile());
//
//                // 저장된 파일 경로를 User 엔티티의 profileImage 필드에 설정
//                user.setProfileImage("/profileImages/" + fileName); // 문자열 경로로 저장
//            } catch (IOException e) {
//                e.printStackTrace();
//                return "register"; // 파일 저장 오류 발생 시 다시 회원가입 페이지로 이동
//            }
//        }
//
//        // 사용자 정보를 데이터베이스에 저장하는 로직 추가
//        user.setUserGroup("GENERAL");
//        userService.save(user);
//
//        // 회원 가입 후 자동 로그인
//        session.setAttribute("loggedInUser", user); // 세션에 사용자 정보 저장
//        return "redirect:/board/list?registered=true"; // 회원가입 완료 시 list 페이지로 리디렉션
//    }
}
