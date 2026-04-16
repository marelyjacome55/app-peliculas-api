package com.example.demo.models;

import jakarta.persistence.*;

@Entity
@Table(name = "pelicula")
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String portada;
    private String genero;
    private Double calificacion;
    private Boolean vista;

    public Pelicula() {
    }

    public Pelicula(Long id, String nombre, String portada, String genero, Double calificacion, Boolean vista) {
        this.id = id;
        this.nombre = nombre;
        this.portada = portada;
        this.genero = genero;
        this.calificacion = calificacion;
        this.vista = vista;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    public Boolean getVista() {
        return vista;
    }

    public void setVista(Boolean vista) {
        this.vista = vista;
    }
}