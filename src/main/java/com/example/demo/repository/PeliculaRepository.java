package com.example.demo.repository;

import com.example.demo.models.Pelicula;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {

    List<Pelicula> findByNombreContainingIgnoreCase(String nombre);

    List<Pelicula> findByVista(boolean vista);

    List<Pelicula> findByUser(User user);

    List<Pelicula> findByUserAndNombreContainingIgnoreCase(User user, String nombre);

    List<Pelicula> findByUserAndVista(User user, boolean vista);

    Optional<Pelicula> findByIdAndUser(Long id, User user);
}