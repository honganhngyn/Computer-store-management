package com.store.controllers;

import com.store.models.UserCredential;
import com.store.services.UserCredentialService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

@Controller
public class UserCredentialController {

    private final UserCredentialService userCredentialService;

    public UserCredentialController(UserCredentialService userCredentialService) {
        this.userCredentialService = userCredentialService;
    }

    // Hiển thị trang login
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Tên file HTML trong thư mục resources/templates (nếu dùng Thymeleaf)
    }

    // Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpSession session) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        return userCredentialService.login(username, password)
                .map(user -> {
                    session.setAttribute("user", user);
                    return ResponseEntity.ok((Object) user);
                }) // Trả về thông tin người dùng nếu đăng nhập thành công
                .orElse(ResponseEntity.badRequest()
                        .body(Map.of("error", "Tài khoản không tồn tại hoặc sai mật khẩu!"))); // Trả về JSON lỗi nếu
                                                                                               // thất bại
    }

    // Hiển thị trang đăng ký
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // Tên file HTML (nếu dùng Thymeleaf thì file phải nằm trong thư mục templates)
    }

    // Đăng ký
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserCredential userCredential) {
        try {
            userCredentialService.register(userCredential);
            return ResponseEntity.ok("Đăng ký thành công!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Xóa thông tin người dùng khỏi session
        return "redirect:/login"; // Chuyển hướng người dùng về trang login
    }
}