package com.recipemanagementsystem.demo.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.recipemanagementsystem.demo.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    @JsonProperty("access_token")
    private String accessToken;
    private Role role;
    private Long userId;
}
