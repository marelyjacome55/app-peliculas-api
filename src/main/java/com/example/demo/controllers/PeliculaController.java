package com.example.demo.controllers;

import com.example.demo.models.Pelicula;
import com.example.demo.repository.PeliculaRepository;
import com.example.demo.service.CloudinaryService;
import org.springframework.http.ResponseEntity;
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

    public PeliculaController(PeliculaRepository peliculaRepository, CloudinaryService cloudinaryService) {
        this.peliculaRepository = peliculaRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> crearPelicula(
            @RequestParam("nombre") String nombre,
            @RequestParam("genero") String genero,
            @RequestParam("calificacion") Double calificacion,
            @RequestParam("vista") boolean vista,
            @RequestParam("imagen") MultipartFile imagen
    ) {
        try {
            String imageUrl = cloudinaryService.subirImagen(imagen);

            Pelicula pelicula = new Pelicula();
            pelicula.setNombre(nombre);
            pelicula.setGenero(genero);
            pelicula.setCalificacion(calificacion);
            pelicula.setVista(vista);
            pelicula.setPortada(imageUrl);

            return ResponseEntity.ok(peliculaRepository.save(pelicula));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear película: " + e.getMessage());
        }
    }

    @GetMapping
    public List<Pelicula> listarPeliculas() {
        return peliculaRepository.findAll();
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> editarPelicula(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("genero") String genero,
            @RequestParam("calificacion") Double calificacion,
            @RequestParam("vista") boolean vista,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen
    ) {
        try {
            Optional<Pelicula> peliculaOptional = peliculaRepository.findById(id);

            if (peliculaOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
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
    public ResponseEntity<Void> eliminarPelicula(@PathVariable Long id) {
        if (!peliculaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        peliculaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public List<Pelicula> buscarPorNombre(@RequestParam String nombre) {
        return peliculaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @GetMapping("/filtrar")
    public List<Pelicula> filtrarPorVista(@RequestParam boolean vista) {
        return peliculaRepository.findByVista(vista);
    }

    @PatchMapping("/{id}/vista")
    public ResponseEntity<Pelicula> cambiarEstadoVista(@PathVariable Long id, @RequestParam boolean vista) {
        Optional<Pelicula> peliculaOptional = peliculaRepository.findById(id);

        if (peliculaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pelicula pelicula = peliculaOptional.get();
        pelicula.setVista(vista);

        return ResponseEntity.ok(peliculaRepository.save(pelicula));
    }
}