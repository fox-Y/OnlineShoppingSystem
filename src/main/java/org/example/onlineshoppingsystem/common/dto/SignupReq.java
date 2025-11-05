package org.example.onlineshoppingsystem.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupReq {

    @NotBlank
    private String username;

    @Size(min = 1, max = 100)
    private String password;

    @Email
    @NotBlank
    private String email;
}
