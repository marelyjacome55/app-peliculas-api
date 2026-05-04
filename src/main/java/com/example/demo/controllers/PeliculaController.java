package com.example.demo.controllers;

// PATRÓN MVC - CONTROLLER:
// Recibe las solicitudes HTTP del frontend para gestionar películas.
// Delega la lógica de negocio al servicio y repositorio correspondiente.
import com.example.demo.models.Pelicula;
import com.example.demo.models.User;
import com.example.demo.repository.PeliculaRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CloudinaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/peliculas")
@CrossOrigin(origins = "*")
public class PeliculaController {

    private final PeliculaRepository peliculaRepository;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;

    public PeliculaController(PeliculaRepository peliculaRepository,
                              CloudinaryService cloudinaryService,
                              UserRepository userRepository) {
        this.peliculaRepository = peliculaRepository;
        this.cloudinaryService = cloudinaryService;
        this.userRepository = userRepository;
    }

    private User obtenerUsuarioAutenticado(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));
    }

    private boolean esAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> crearPelicula(
            @RequestParam("nombre") String nombre,
            @RequestParam("genero") String genero,
            @RequestParam("calificacion") Double calificacion,
            @RequestParam("vista") boolean vista,
            @RequestParam("imagen") MultipartFile imagen,
            Authentication authentication
    ) {
        try {
            User user = obtenerUsuarioAutenticado(authentication);
            String imageUrl = cloudinaryService.subirImagen(imagen);

            Pelicula pelicula = new Pelicula();
            pelicula.setNombre(nombre);
            pelicula.setGenero(genero);
            pelicula.setCalificacion(calificacion);
            pelicula.setVista(vista);
            pelicula.setPortada(imageUrl);
            pelicula.setUser(user);

            return ResponseEntity.ok(peliculaRepository.save(pelicula));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear película: " + e.getMessage());
        }
    }

    @GetMapping
    public List<Pelicula> listarPeliculas(Authentication authentication) {
        User user = obtenerUsuarioAutenticado(authentication);

        if (esAdmin(authentication)) {
            return peliculaRepository.findAll();
        }

        return peliculaRepository.findByUser(user);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> editarPelicula(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("genero") String genero,
            @RequestParam("calificacion") Double calificacion,
            @RequestParam("vista") boolean vista,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            Authentication authentication
    ) {
        try {
            User user = obtenerUsuarioAutenticado(authentication);

            Optional<Pelicula> peliculaOptional;
            if (esAdmin(authentication)) {
                peliculaOptional = peliculaRepository.findById(id);
            } else {
                peliculaOptional = peliculaRepository.findByIdAndUser(id, user);
            }

            if (peliculaOptional.isEmpty()) {
                return ResponseEntity.status(404).body("Película no encontrada o no te pertenece");
            }

            Pelicula pelicula = peliculaOptional.get();
            pelicula.setNombre(nombre);
            pelicula.setGenero(genero);
            pelicula.setCalificacion(calificacion);
            pelicula.setVista(vista);

            if (imagen != null && !imagen.isEmpty()) {
                String imageUrl = cloudinaryService.subirImagen(imagen);
                pelicula.setPortada(imageUrl);
            }

            return ResponseEntity.ok(peliculaRepository.save(pelicula));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al editar película: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPelicula(@PathVariable Long id, Authentication authentication) {
        User user = obtenerUsuarioAutenticado(authentication);

        Optional<Pelicula> peliculaOptional;
        if (esAdmin(authentication)) {
            peliculaOptional = peliculaRepository.findById(id);
        } else {
            peliculaOptional = peliculaRepository.findByIdAndUser(id, user);
        }

        if (peliculaOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Película no encontrada o no te pertenece");
        }

        peliculaRepository.delete(peliculaOptional.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public List<Pelicula> buscarPorNombre(@RequestParam String nombre, Authentication authentication) {
        User user = obtenerUsuarioAutenticado(authentication);

        if (esAdmin(authentication)) {
            return peliculaRepository.findByNombreContainingIgnoreCase(nombre);
        }

        return peliculaRepository.findByUserAndNombreContainingIgnoreCase(user, nombre);
    }

    @GetMapping("/filtrar")
    public List<Pelicula> filtrarPorVista(@RequestParam boolean vista, Authentication authentication) {
        User user = obtenerUsuarioAutenticado(authentication);

        if (esAdmin(authentication)) {
            return peliculaRepository.findByVista(vista);
        }

        return peliculaRepository.findByUserAndVista(user, vista);
    }

    @PatchMapping("/{id}/vista")
    public ResponseEntity<?> cambiarEstadoVista(
            @PathVariable Long id,
            @RequestParam boolean vista,
            Authentication authentication
    ) {
        User user = obtenerUsuarioAutenticado(authentication);

        Optional<Pelicula> peliculaOptional;
        if (esAdmin(authentication)) {
            peliculaOptional = peliculaRepository.findById(id);
        } else {
            peliculaOptional = peliculaRepository.findByIdAndUser(id, user);
        }

        if (peliculaOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Película no encontrada o no te pertenece");
        }

        Pelicula pelicula = peliculaOptional.get();
        pelicula.setVista(vista);

        return ResponseEntity.ok(peliculaRepository.save(pelicula));
    }
}