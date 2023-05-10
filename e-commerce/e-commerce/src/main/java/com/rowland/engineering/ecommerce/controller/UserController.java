package com.rowland.engineering.ecommerce.controller;

import com.rowland.engineering.ecommerce.dto.ApiResponse;
import com.rowland.engineering.ecommerce.dto.RegisterRequest;
import com.rowland.engineering.ecommerce.model.User;
import com.rowland.engineering.ecommerce.repository.UserRepository;
import com.rowland.engineering.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;


    @DeleteMapping("users/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")  //@RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long id){
        Optional<User> userId = userRepository.findById(id);
        System.out.println(userId + " USER ID");

        userRepository.deleteById(id);

        return new ResponseEntity<>(new ApiResponse(true, "User has been deleted"), HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable(value = "id") Long id) {
        return userService.findUserById(id);
    }

    @PutMapping("/update/{id}")
    public Optional<User> updateUserById(@RequestBody RegisterRequest update,
                                         @PathVariable(value = "id") Long id) {
        return userService.updateUserById(update, id);
    }

    @GetMapping("/all")
    public List<User> getUsers(){
        return userService.getAllUsers();
    }
}
