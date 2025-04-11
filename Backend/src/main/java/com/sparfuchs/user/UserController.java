package com.sparfuchs.user;

import com.sparfuchs.DTO.AuthRequestDTO;
import com.sparfuchs.DTO.UserResponseDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody AuthRequestDTO request) {
        UserResponseDTO user = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody AuthRequestDTO request, HttpSession session) {
        UserResponseDTO user = userService.login(request, session);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        userService.logout(session);
        return ResponseEntity.ok().build();
    }
}
