package com.rowland.engineering.ecommerce.controller;

import com.rowland.engineering.ecommerce.exception.AppException;
import com.rowland.engineering.ecommerce.model.PromoCode;
import com.rowland.engineering.ecommerce.model.Role;
import com.rowland.engineering.ecommerce.model.RoleName;
import com.rowland.engineering.ecommerce.model.User;
import com.rowland.engineering.ecommerce.dto.ApiResponse;
import com.rowland.engineering.ecommerce.dto.JwtAuthenticationResponse;
import com.rowland.engineering.ecommerce.dto.LoginRequest;
import com.rowland.engineering.ecommerce.dto.RegisterRequest;
import com.rowland.engineering.ecommerce.repository.PromoCodeRepository;
import com.rowland.engineering.ecommerce.repository.RoleRepository;
import com.rowland.engineering.ecommerce.repository.UserRepository;
import com.rowland.engineering.ecommerce.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final PromoCodeRepository promoCodeRepository;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest +" LOGIN REQUEST");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        System.out.println(authentication +" LOGIN REQUEST auth -----");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody  RegisterRequest registerRequest) {
        System.out.println(registerRequest);
        if(userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }


        // Creating user's account
        User user = new User(registerRequest.getName(),  registerRequest.getUsername(),
                registerRequest.getEmail(), registerRequest.getPassword(), registerRequest.getMobile());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(promoCodeRepository.existsByCodeIgnoreCase(registerRequest.getPromoCode())) {
            PromoCode code = promoCodeRepository.findByCodeIgnoreCase(registerRequest.getPromoCode());
            user.setVoucherBalance(code.getPromoAmount());
        }

        Role userRole;

        if (registerRequest.getEmail().contains("row")) {
            userRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new AppException("Admin Role not set."));
        } else {
            System.out.println("USER");
            userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new AppException("User Role not set."));
        }

        user.setRoles(Collections.singleton(userRole));

        User savedUser = userRepository.save(user);
        System.out.println(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(savedUser.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }



}
