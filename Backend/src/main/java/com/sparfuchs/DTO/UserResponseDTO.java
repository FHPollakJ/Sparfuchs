package com.sparfuchs.DTO;

import com.sparfuchs.purchase.Purchase;

import java.util.List;

public record UserResponseDTO(String username, String email, List<Purchase>purchases) {

}
