package com.example.demo.repository;

import com.example.demo.models.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {

    List<Pelicula> findByNombreContainingIgnoreCase(String nombre);

    List<Pelicula> findByVista(boolean vista);
}