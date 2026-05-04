package com.example.demo.payload.request;

// PATRÓN DTO - DATA TRANSFER OBJECT:
// Recibe desde el frontend el comentario personal de una película.
// Evita modificar directamente la entidad Pelicula exponiendo solo el cambio necesario.
public class ComentarioPersonalRequest {

    private String comentarioPersonal;

    public ComentarioPersonalRequest() {
    }

    public ComentarioPersonalRequest(String comentarioPersonal) {
        this.comentarioPersonal = comentarioPersonal;
    }

    public String getComentarioPersonal() {
        return comentarioPersonal;
    }

    public void setComentarioPersonal(String comentarioPersonal) {
        this.comentarioPersonal = comentarioPersonal;
    }
}