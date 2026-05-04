package com.example.demo.repository;

// PATRÓN REPOSITORY:
// Separa el acceso a datos de la lógica de negocio.
// Spring Data JPA genera automáticamente las consultas a partir del nombre de los métodos.
import com.example.demo.models.ERole;
import com.example.demo.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}