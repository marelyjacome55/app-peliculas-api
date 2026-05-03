package com.example.demo.payload.response;

// PATRÓN DTO:
// Este objeto representa una película dentro de una colección de reacción.
// Ejemplo: todas las películas guardadas en "Me encanta".
public class PeliculaEnReaccionResponse {

    private Long id;
    private String nombre;
    private String portada;
    private String genero;
    private Double calificacion;
    private Boolean vista;
    private String comentarioPersonal;

    public PeliculaEnReaccionResponse() {
    }

    public PeliculaEnReaccionResponse(Long id, String nombre, String portada, String genero,
                                      Double calificacion, Boolean vista, String comentarioPersonal) {
        this.id = id;
        this.nombre = nombre;
        this.portada = portada;
        this.genero = genero;
        this.calificacion = calificacion;
        this.vista = vista;
        this.comentarioPersonal = comentarioPersonal;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPortada() {
        return portada;
    }

    public String getGenero() {
        return genero;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public Boolean getVista() {
        return vista;
    }

    public String getComentarioPersonal() {
        return comentarioPersonal;
    }
}