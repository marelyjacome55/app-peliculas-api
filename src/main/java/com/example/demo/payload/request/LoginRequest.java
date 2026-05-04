package com.example.demo.payload.request;

// PATRÓN DTO - DATA TRANSFER OBJECT:
// Transporta las credenciales del usuario desde el frontend.
// Valida que usuario y contraseña no estén vacíos antes de procesarse.
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}