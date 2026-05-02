package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "comentario_personal", length = 1000)
    private String comentarioPersonal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Pelicula() {
    }

    public Pelicula(Long id, String nombre, String portada, String genero, Double calificacion, Boolean vista, String comentarioPersonal, User user) {
        this.id = id;
        this.nombre = nombre;
        this.portada = portada;
        this.genero = genero;
        this.calificacion = calificacion;
        this.vista = vista;
        this.comentarioPersonal = comentarioPersonal;
        this.user = user;
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

    public String getComentarioPersonal() {
        return comentarioPersonal;
    }

    public void setComentarioPersonal(String comentarioPersonal) {
        this.comentarioPersonal = comentarioPersonal;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}