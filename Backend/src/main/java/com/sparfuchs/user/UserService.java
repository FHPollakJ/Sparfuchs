package com.sparfuchs.user;

import com.sparfuchs.DTO.AuthRequestDTO;
import com.sparfuchs.DTO.UserResponseDTO;
import com.sparfuchs.exception.BadRequestException;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO register(AuthRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already in use.");
        }

        User user = new User(request.getEmail(), passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return new UserResponseDTO(user.getId(), user.getEmail());
    }

    public UserResponseDTO login(AuthRequestDTO request, HttpSession session) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        session.setAttribute("userId", user.getId());

        return new UserResponseDTO(user.getId(), user.getEmail());
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}

