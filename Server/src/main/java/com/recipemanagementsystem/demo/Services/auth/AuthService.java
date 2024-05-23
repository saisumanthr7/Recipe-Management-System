package com.recipemanagementsystem.demo.Services.auth;

import com.recipemanagementsystem.demo.Dto.Auth.AuthenticationRequest;
import com.recipemanagementsystem.demo.Dto.Auth.AuthenticationResponse;
import com.recipemanagementsystem.demo.Dto.Auth.SignupRequest;

public interface AuthService {

    AuthenticationResponse register(SignupRequest signupRequest);
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
    boolean customerEmailExists(String email);

}
