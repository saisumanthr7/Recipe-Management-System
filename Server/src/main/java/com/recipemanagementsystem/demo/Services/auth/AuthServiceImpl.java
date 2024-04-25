package com.recipemanagementsystem.demo.Services.auth;

import com.recipemanagementsystem.demo.Dto.AuthenticationResponse;
import com.recipemanagementsystem.demo.Dto.SignupRequest;
import com.recipemanagementsystem.demo.Entity.User;
import com.recipemanagementsystem.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    @Override
    public AuthenticationResponse register(SignupRequest signupRequest) {
        var user = User.builder()
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .email(signupRequest.getEmail())
                .password(signupRequest.getPassword())
                .role(signupRequest.getRole())
                .build();
        var savedUser = userRepository.save(user);
        return null;
    }
}