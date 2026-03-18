package com.ecommerce.sb_ecommerce.security.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    @NotBlank
    @Size(min =3)
    private String username;

    @NotBlank
    @Email
    @Size(min =5)
    private String email;

    @NotBlank
    private String password;

    private Set<String> roles;
}
