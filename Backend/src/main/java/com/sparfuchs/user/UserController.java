package com.sparfuchs.user;

import com.sparfuchs.DTO.AuthRequestDTO;
import com.sparfuchs.DTO.PurchaseDTO;
import com.sparfuchs.DTO.UserResponseDTO;
import com.sparfuchs.DTO.UserStatsDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;

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
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(HttpSession session) {
        userService.deleteUser((long) session.getAttribute("userId"));
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/edit")
    public ResponseEntity<UserResponseDTO> editUser(@RequestBody AuthRequestDTO request, HttpSession session) {
        UserResponseDTO updatedUser = userService.editUser(request,(long) session.getAttribute("userId"));
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/getPurchases")
    public ResponseEntity<List<PurchaseDTO>> getUserPurchases(HttpSession session) {
        List<PurchaseDTO> purchases = userService.getPurchasesForUser((long) session.getAttribute("userId"));
        return ResponseEntity.ok(purchases);
    }

    @GetMapping("/getStats")
    public ResponseEntity<UserStatsDTO> getUserStats(HttpSession session) {
        UserStatsDTO purchases = userService.getUserStats((long) session.getAttribute("userId"));
        return ResponseEntity.ok(purchases);
    }



}
