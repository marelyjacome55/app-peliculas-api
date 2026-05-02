package com.example.demo.repository;

import com.example.demo.models.Pelicula;
import com.example.demo.models.PeliculaReaccion;
import com.example.demo.models.TipoReaccion;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// PATRÓN REPOSITORY:
// Se utiliza para separar las operaciones de acceso a datos de la lógica de negocio.
// Spring Data JPA genera automáticamente las consultas necesarias a partir del nombre de los métodos.
public interface PeliculaReaccionRepository extends JpaRepository<PeliculaReaccion, Long> {

    boolean existsByPeliculaAndTipoReaccion(Pelicula pelicula, TipoReaccion tipoReaccion);

    Optional<PeliculaReaccion> findByPeliculaAndTipoReaccion(Pelicula pelicula, TipoReaccion tipoReaccion);

    List<PeliculaReaccion> findByPelicula(Pelicula pelicula);

    long countByPelicula(Pelicula pelicula);

    long countByPelicula_UserAndTipoReaccion(User user, TipoReaccion tipoReaccion);

    List<PeliculaReaccion> findByPelicula_UserAndTipoReaccion(User user, TipoReaccion tipoReaccion);
} 