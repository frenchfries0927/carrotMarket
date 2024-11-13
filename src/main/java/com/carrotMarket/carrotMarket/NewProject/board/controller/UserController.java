package com.carrotMarket.carrotMarket.NewProject.board.controller;

import com.carrotMarket.carrotMarket.NewProject.board.entity.User;
import com.carrotMarket.carrotMarket.NewProject.board.service.UserService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final ServletContext servletContext;

    @Autowired
    public UserController(UserService userService, ServletContext servletContext) {
        this.userService = userService;
        this.servletContext = servletContext;
    }

    //회원가입
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
                               @RequestParam("profileImageFile") MultipartFile profileImageFile,
                               HttpSession session) {
        user.setUserGroup("GENERAL");

        if (!profileImageFile.isEmpty()) {
            String filePath = saveProfileImage(profileImageFile);
            if (filePath == null) {
                return "register";
            }
            user.setProfileImage(filePath);
        }

        userService.save(user);
        session.setAttribute("loggedInUser", user);

        return "redirect:/board/list";
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    private String saveProfileImage(MultipartFile profileImageFile) {
        try {
//            참고 https://chaemin0707.tistory.com/26
//            path = "D:\carrotMarket\."
//            root = "D:\"
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
//            Path normalize = Paths.get(uploadDir).toAbsolutePath().normalize();


            // 디렉토리가 존재하지 않으면 생성
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 파일 이름을 고유하게 설정
            String fileName = System.currentTimeMillis() + "_" + profileImageFile.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            profileImageFile.transferTo(filePath.toFile());

            // 저장된 파일 경로 반환 (정적 리소스 접근 경로로 설정)
            return "/profileImages/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    

    //로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/board/list";
    }

    //로그인(위치정보 수집)
    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) String location,
            HttpSession session) {
        // 회원 인증
        User user = userService.authenticate(email, password);
        if (user != null) {
            // 위치 정보 업데이트
            if (latitude != null && longitude != null && location != null) {
                userService.updateUserLocation(user.getId(), latitude, longitude, location);
            }
            session.setAttribute("loggedInUser", user); // 세션에 사용자 정보 저장
            return "redirect:/board/list";
        } else {
            return "redirect:/board/list?loginError=true";
        }
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



}
