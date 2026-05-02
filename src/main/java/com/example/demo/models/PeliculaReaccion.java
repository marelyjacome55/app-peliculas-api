package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "pelicula_reaccion",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"pelicula_id", "tipo_reaccion"})
        }
)
public class PeliculaReaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con la película.
    // Una película puede tener varias reacciones, pero no puede repetir la misma reacción.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pelicula_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Pelicula pelicula;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_reaccion", nullable = false)
    private TipoReaccion tipoReaccion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    public PeliculaReaccion() {
    }

    public PeliculaReaccion(Pelicula pelicula, TipoReaccion tipoReaccion) {
        this.pelicula = pelicula;
        this.tipoReaccion = tipoReaccion;
        this.fechaCreacion = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public TipoReaccion getTipoReaccion() {
        return tipoReaccion;
    }

    public void setTipoReaccion(TipoReaccion tipoReaccion) {
        this.tipoReaccion = tipoReaccion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
