package com.recipemanagementsystem.demo.Services.auth;

import com.recipemanagementsystem.demo.Dto.AuthenticationResponse;
import com.recipemanagementsystem.demo.Dto.SignupRequest;

public interface AuthService {

    AuthenticationResponse register(SignupRequest signupRequest);

}
