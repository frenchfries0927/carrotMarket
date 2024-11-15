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
import org.springframework.web.client.RestTemplate;
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

    // application.properties에 설정된 API 키를 가져옴
    @Value("${google.api.key}")
    private String apiKey;
    // kakao 주소 api JavaScript 키 가져오기
    @Value("${kakao.api.javascript.key}")
    private String kakaoJavascriptKey;
    //profile upload folder
    @Value("${file.upload-dir}")
    private String uploadDir;

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
                               HttpSession session) throws IOException {
        //중복되는 코드....service 단 saveOrUpdateUser 에서 동일하게 수행.
//        if (!profileImageFile.isEmpty()) {
//            String filePath = saveProfileImage(profileImageFile, user);
//            if (filePath == null) {
//                return "register";
//            }
//            user.setProfileImage(filePath);
//        }

//        userService.save(user);
//        userService.saveOrUpdateUser(user, profileImageFile);
//        session.setAttribute("loggedInUser", user);
//
//        return "redirect:/board/list";

        try {
            userService.saveOrUpdateUser(user, profileImageFile);
            session.setAttribute("loggedInUser", user);
            return "redirect:/board/list"; // 회원가입 완료 후 로그인 페이지로 리디렉션
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/register?error=true";
        }
    }

//    private String saveProfileImage(MultipartFile profileImageFile, User user){
//        try {
////            참고 https://chaemin0707.tistory.com/26
//            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
//
//            // 디렉토리가 존재하지 않으면 생성
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//
//            // 파일 확장자 추출
//            String originalFileName = profileImageFile.getOriginalFilename();
//            String fileExtension = null;
//            if (originalFileName != null) {
//                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
//            }
//
//            // 사용자 이메일과 파일 확장자를 포함하여 파일 이름을 고유하게 설정
//            String fileName = user.getEmail() + fileExtension;
//            Path filePath = uploadPath.resolve(fileName);
//
//            // 동일한 이름의 파일이 있으면 삭제하여 덮어쓰기 준비
//            if (Files.exists(filePath)) {
//                Files.delete(filePath);
//            }
//
//            // 파일 저장
//            profileImageFile.transferTo(filePath.toFile());
//
//            // 저장된 파일 경로 반환 (정적 리소스 접근 경로로 설정)
//            return "/profileImages/" + fileName;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    

    //로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/board/list";
    }
    @GetMapping("/get-address")
    @ResponseBody
    public String getAddress(@RequestParam double latitude, @RequestParam double longitude) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&key=" + apiKey+ "&language=ko";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);

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
    //프로필 수정하기.
    @GetMapping("/edit-profile")
    public String editProfile(Model model, HttpSession session) {
        User user = userService.getLoggedInUser(session);
        model.addAttribute("user", user);
        // Kakao JavaScript API 키를 모델에 추가
        model.addAttribute("kakaoJavascriptKey", kakaoJavascriptKey);
        return "edit-profile";
    }
    // 프로필 업데이트
    @PostMapping("/edit-profile")
    public String updateProfile(@ModelAttribute User user,
                                @RequestParam("currentPassword") String currentPassword,
                                @RequestParam("profileImageFile") MultipartFile profileImageFile,
                                HttpSession session) {
        try {
            // 기존 사용자의 ID, group 설정
            user.setId(userService.getLoggedInUser(session).getId());
            user.setUserGroup(userService.getLoggedInUser(session).getUserGroup());

            User loggedInUser = userService.getLoggedInUser(session);

//            // 현재 비밀번호 확인
//            if (!loggedInUser.getPassword().equals(currentPassword)) {
//                return "redirect:/edit-profile?error=passwordMismatch";
//            }

            // 새 비밀번호가 입력되지 않은 경우 현재 비밀번호로 유지
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(loggedInUser.getPassword());
            }

            // 프로필 이미지와 기타 정보 업데이트
            userService.saveOrUpdateUser(user, profileImageFile);

            // 세션에 업데이트된 사용자 정보 저장
            session.setAttribute("loggedInUser", user);
            return "redirect:/board/list"; // 프로필 페이지로 리디렉션

        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/edit-profile?error=true";
        }
    }
    @PostMapping("/check-password")
    @ResponseBody
    public boolean checkPassword(@RequestParam("currentPassword") String currentPassword, HttpSession session) {
        User loggedInUser = userService.getLoggedInUser(session);
        return loggedInUser != null && loggedInUser.getPassword().equals(currentPassword);
    }


    //입력받은 주소로 위도,경도 갱신하기.
    @GetMapping("/get-latlng")
    @ResponseBody
    public String getLatLng(@RequestParam("address") String address) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        return response; // API의 JSON 응답을 클라이언트에 반환
    }



}
