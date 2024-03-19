package com.pichincha.app.security.dto;

import com.pichincha.app.security.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateUserDto {
    @NotBlank(message = "username es requerido")
    private String username;

    @NotBlank(message = "password es requerido")
    private String password;
    @NotBlank(message = "rol es requerido")
    private String role;
}
