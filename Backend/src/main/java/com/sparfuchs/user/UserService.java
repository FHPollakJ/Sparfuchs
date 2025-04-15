package com.sparfuchs.user;

import com.sparfuchs.DTO.AuthRequestDTO;
import com.sparfuchs.DTO.UserResponseDTO;
import com.sparfuchs.exception.BadRequestException;
import com.sparfuchs.exception.NotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO register(AuthRequestDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email is already in use.");
        }

        User user = new User(request.username(),request.email(), passwordEncoder.encode(request.password()));
        userRepository.save(user);
        return new UserResponseDTO(user.getUsername(),user.getEmail(),user.getPurchases());
    }

    public UserResponseDTO login(AuthRequestDTO request, HttpSession session) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        session.setAttribute("userId", user.getId());

        return new UserResponseDTO(user.getUsername(),user.getEmail(),user.getPurchases());
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    public void deleteUser(HttpSession session) {
        User user = userRepository.findById((long)session.getAttribute("userId"))
                .orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.delete(user);
    }

    public UserResponseDTO editUser(AuthRequestDTO request, HttpSession session) {
        User user = userRepository.findById((long)session.getAttribute("userId"))
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (request.username() != null) {
            user.setUsername(request.username());
        }
        if (request.email() != null) {
            if(userRepository.findByEmail(request.email()).isPresent()){
                throw new BadRequestException("Email is already in use.");
            }
            user.setEmail(request.email());
        }
        if (request.password() != null) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        userRepository.save(user);

        return new UserResponseDTO(user.getUsername(),user.getEmail(),user.getPurchases());
    }

}

