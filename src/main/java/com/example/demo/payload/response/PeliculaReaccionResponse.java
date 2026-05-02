package com.example.demo.payload.response;

// PATRÓN DTO:
// Este objeto se usa para enviar al frontend una reacción seleccionada,
// mostrando tanto el valor interno como el nombre visible.
public class PeliculaReaccionResponse {

    private String tipoReaccion;
    private String nombre;

    public PeliculaReaccionResponse() {
    }

    public PeliculaReaccionResponse(String tipoReaccion, String nombre) {
        this.tipoReaccion = tipoReaccion;
        this.nombre = nombre;
    }

    public String getTipoReaccion() {
        return tipoReaccion;
    }

    public void setTipoReaccion(String tipoReaccion) {
        this.tipoReaccion = tipoReaccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}