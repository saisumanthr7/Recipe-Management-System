package com.recipemanagementsystem.demo.Services.auth;

import com.recipemanagementsystem.demo.Configuration.JWTService;
import com.recipemanagementsystem.demo.Dto.Auth.AuthenticationRequest;
import com.recipemanagementsystem.demo.Dto.Auth.AuthenticationResponse;
import com.recipemanagementsystem.demo.Dto.Auth.SignupRequest;
import com.recipemanagementsystem.demo.Entity.User;
import com.recipemanagementsystem.demo.Enums.Role;
import com.recipemanagementsystem.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    @Override
    public AuthenticationResponse register(SignupRequest signupRequest) {

        if (customerEmailExists(signupRequest.getEmail())) {
            return AuthenticationResponse.builder().errorMessage("Email already exists!").build();
        }else {
            var user = User.builder()
                    .firstName(signupRequest.getFirstName())
                    .lastName(signupRequest.getLastName())
                    .email(signupRequest.getEmail())
                    .password(passwordEncoder.encode(signupRequest.getPassword()))
                    .role(Role.MEMBER)
                    .build();
            var savedUser = userRepository.save(user);
            String jwtToken = jwtService.generateToken(savedUser);
            return AuthenticationResponse.builder().accessToken(jwtToken).build();
        }
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest)
            throws BadCredentialsException,
            DisabledException, UsernameNotFoundException {
       try {
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           authenticationRequest.getEmail(),
                           authenticationRequest.getPassword()
                   )
           );
       }catch (BadCredentialsException e){
           throw new BadCredentialsException("Incorrect Username or Password entered!");
       }

        var user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow();
        Optional<User> optionalUser = userRepository.findByEmail(user.getUsername());
        String jwtToken = jwtService.generateToken(user);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if(optionalUser.isPresent()){
            authenticationResponse.setAccessToken(jwtToken);
            authenticationResponse.setRole(optionalUser.get().getRole());
            authenticationResponse.setUserId(optionalUser.get().getId());
        }
        return authenticationResponse;
    }

    @Override
    public boolean customerEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }


}
