//package com.rowland.engineering.ecommerce.controller;
//
//
//import com.rowland.engineering.ecommerce.config.JwtService;
//import com.rowland.engineering.ecommerce.dto.LoginRequest;
//import com.rowland.engineering.ecommerce.dto.RegisterRequest;
//import com.rowland.engineering.ecommerce.exception.AppException;
//import com.rowland.engineering.ecommerce.model.Role;
//import com.rowland.engineering.ecommerce.model.User;
////import com.rowland.engineering.ecommerce.repository.RoleRepository;
//import com.rowland.engineering.ecommerce.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.net.URI;
//
//@Service
//@RequiredArgsConstructor
//public class AuthenticationService {
//    private final JwtService jwtService;
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
////    private final RoleRepository roleRepository;
//
//    private final AuthenticationManager authenticationManager;
//    public AuthenticationResponse register(RegisterRequest registerRequest) {
//        Role userRole = registerRequest.getEmail().contains("row") ? Role.ROLE_ADMIN : Role.ROLE_ADMIN;
//
//
//        User user = User.builder()
//                .name(registerRequest.getName())
//                .email(registerRequest.getEmail())
//                .mobile(registerRequest.getMobile())
//                .username(registerRequest.getUsername())
//                .password(passwordEncoder.encode(registerRequest.getPassword()))
//                .role(userRole)
//                .build();
//        userRepository.save(user);
//        var jwtToken = jwtService.generateToken(user);
//        return AuthenticationResponse.builder()
//                .token(jwtToken)
//                .build();
//    }
//
//    public AuthenticationResponse login(LoginRequest loginRequest) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getEmail(),
//                        loginRequest.getPassword()
//                )
//        );
//        var user = userRepository.findByEmail(loginRequest.getEmail())
//                .orElseThrow(() ->new UsernameNotFoundException("User email not found, though to get to this point user must hav been authenticated"));
//
//        var jwtToken = jwtService.generateToken(user);
//        return AuthenticationResponse.builder()
//                .token(jwtToken)
//                .build();
//    }
//}
//
////
////    private final PasswordEncoder passwordEncoder;
////
////    private final JwtTokenProvider tokenProvider;
//////    public AuthenticationResponse login(LoginRequest loginRequest) {
//////
//////    }
////
//////    public AuthenticationResponse register(RegisterRequest registerRequest) {
//////        User user = User.builder()
//////                .name(registerRequest.getName())
//////                .username(registerRequest.getUsername())
//////                .mobile(registerRequest.getMobile())
//////                .password(passwordEncoder.encode(registerRequest.getPassword()))
//////                .email(registerRequest.getEmail())
//////                .build();
//////
//////        userRepository.save(user);
//////
//////    }
////}
