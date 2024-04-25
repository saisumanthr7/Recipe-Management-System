package com.recipemanagementsystem.demo.Controller;

import com.recipemanagementsystem.demo.Dto.AuthenticationResponse;
import com.recipemanagementsystem.demo.Dto.SignupRequest;
import com.recipemanagementsystem.demo.Services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("RMS/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signUp(
            @RequestBody SignupRequest signupRequest
            ){
        AuthenticationResponse authenticationResponse = authService.register(signupRequest);
        return ResponseEntity.ok(authenticationResponse);
    }
}
