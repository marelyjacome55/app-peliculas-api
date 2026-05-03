package com.example.demo.payload.request;

// PATRÓN DTO:
// Este objeto recibe desde el frontend el comentario personal de una película.
// Se usa para no modificar directamente la entidad Pelicula desde el controlador.
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