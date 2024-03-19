package com.pichincha.app.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginDto {
    @NotBlank(message = "username es requerido")
    private String username;
    @NotBlank(message = "password es requerido")
    private String password;
}
