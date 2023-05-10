package com.rowland.engineering.ecommerce.service;

import com.rowland.engineering.ecommerce.dto.RegisterRequest;
import com.rowland.engineering.ecommerce.model.User;
import com.rowland.engineering.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> updateUserById(RegisterRequest update, Long id) {
        System.out.println("update update");
        return userRepository.findById(id).map(
                user -> {
                    user.setUsername(update.getUsername());
                    user.setName(update.getName());
                    user.setEmail(update.getEmail());
                    user.setMobile(update.getMobile());
                    return userRepository.save(user);
                }
        );
    }
}
