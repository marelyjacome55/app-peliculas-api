package com.example.demo.payload.response;

// PATRÓN DTO - DATA TRANSFER OBJECT:
// Transporta mensajes de respuesta general hacia el frontend.
// Utilizado en respuestas de operaciones que solo requieren confirmación textual.
public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}