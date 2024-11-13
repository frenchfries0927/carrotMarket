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
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();


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

<<<<<<< HEAD
    //로그인
//    @PostMapping("/login")
//    public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
//        User user = userService.authenticate(email, password);
//        if (user != null) {
//            session.setAttribute("loggedInUser", user); // 세션에 사용자 정보 저장
//            return "redirect:/board/list";
//        } else {
//           // return "redirect:/?loginError=true";
//            return "redirect:/board/list?loginError=true"; // 로그인 실패 시 loginError 매개변수 추가
//        }
//    }
    //로그아웃
=======
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        User user = userService.authenticate(email, password);
        if (user != null) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/board/list";
        } else {
            return "redirect:/board/list?loginError=true";
        }
    }

>>>>>>> f089338 (trash)
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/board/list";
    }
<<<<<<< HEAD
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
=======

>>>>>>> f089338 (trash)
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
<<<<<<< HEAD




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
=======
>>>>>>> f089338 (trash)
}
