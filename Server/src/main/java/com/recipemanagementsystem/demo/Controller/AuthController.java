package com.recipemanagementsystem.demo.Controller;

import com.recipemanagementsystem.demo.Dto.Auth.AuthenticationRequest;
import com.recipemanagementsystem.demo.Dto.Auth.AuthenticationResponse;
import com.recipemanagementsystem.demo.Dto.Auth.SignupRequest;
import com.recipemanagementsystem.demo.Services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("RMS/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signUp(
            @RequestBody SignupRequest signupRequest){
        AuthenticationResponse authenticationResponse = authService.register(signupRequest);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest authenticationRequest)
    {
        return ResponseEntity.ok(authService.authenticate(authenticationRequest));
    }
}
