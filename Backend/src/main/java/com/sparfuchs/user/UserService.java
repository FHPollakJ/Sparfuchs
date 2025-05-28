package com.sparfuchs.user;

import com.sparfuchs.DTO.AuthRequestDTO;
import com.sparfuchs.DTO.PurchaseDTO;
import com.sparfuchs.DTO.UserResponseDTO;
import com.sparfuchs.DTO.UserStatsDTO;
import com.sparfuchs.exception.BadRequestException;
import com.sparfuchs.exception.ForbiddenException;
import com.sparfuchs.exception.NotFoundException;
import com.sparfuchs.purchase.PurchaseService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PurchaseService purchaseService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, PurchaseService purchaseService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.purchaseService = purchaseService;
    }
    @Transactional
    public UserResponseDTO register(AuthRequestDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email is already in use.");
        }

        User user = new User(request.username(),request.email(), passwordEncoder.encode(request.password()));
        userRepository.save(user);
        return new UserResponseDTO(user.getUsername(),user.getEmail());
    }

    public UserResponseDTO login(AuthRequestDTO request, HttpSession session) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ForbiddenException("Invalid credentials."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ForbiddenException("Invalid email or password.");
        }

        session.setAttribute("userId", user.getId());

        return new UserResponseDTO(user.getUsername(),user.getEmail());
    }

    public void deleteUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.delete(user);
    }

    @Transactional
    public UserResponseDTO editUser(AuthRequestDTO request, long userId) {
        User user = userRepository.findById(userId)
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

        return new UserResponseDTO(user.getUsername(),user.getEmail());
    }

    public UserStatsDTO getUserStats(long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return new UserStatsDTO(user.getTotalAmountSpent(),user.getTotalAmountSaved());
    }

    public List<PurchaseDTO> getPurchasesForUser(long userId) {
        return purchaseService.getPurchasesForUser(userId);
    }
}

