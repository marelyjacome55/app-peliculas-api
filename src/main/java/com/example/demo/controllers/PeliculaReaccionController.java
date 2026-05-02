package com.example.demo.controllers;

import com.example.demo.payload.request.ComentarioPersonalRequest;
import com.example.demo.service.PeliculaReaccionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

// PATRÓN MVC - CONTROLLER:
// Este controlador recibe las solicitudes HTTP del frontend y delega la lógica al servicio.
// No contiene reglas de negocio directamente; solo coordina entrada y salida de datos.
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PeliculaReaccionController {

    private final PeliculaReaccionService peliculaReaccionService;

    public PeliculaReaccionController(PeliculaReaccionService peliculaReaccionService) {
        this.peliculaReaccionService = peliculaReaccionService;
    }

    @PatchMapping("/peliculas/{id}/comentario")
    public ResponseEntity<?> actualizarComentarioPersonal(
            @PathVariable Long id,
            @RequestBody ComentarioPersonalRequest request,
            Authentication authentication
    ) {
        try {
            return ResponseEntity.ok(
                    peliculaReaccionService.actualizarComentarioPersonal(id, request, authentication)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/peliculas/{id}/reacciones")
    public ResponseEntity<?> obtenerReaccionesDePelicula(
            @PathVariable Long id,
            Authentication authentication
    ) {
        try {
            return ResponseEntity.ok(
                    peliculaReaccionService.obtenerReaccionesDePelicula(id, authentication)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/peliculas/{id}/reacciones/{tipoReaccion}")
    public ResponseEntity<?> agregarReaccion(
            @PathVariable Long id,
            @PathVariable String tipoReaccion,
            Authentication authentication
    ) {
        try {
            return ResponseEntity.ok(
                    peliculaReaccionService.agregarReaccion(id, tipoReaccion, authentication)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/peliculas/{id}/reacciones/{tipoReaccion}")
    public ResponseEntity<?> eliminarReaccion(
            @PathVariable Long id,
            @PathVariable String tipoReaccion,
            Authentication authentication
    ) {
        try {
            return ResponseEntity.ok(
                    peliculaReaccionService.eliminarReaccion(id, tipoReaccion, authentication)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/peliculas/{id}/reacciones/contador")
    public ResponseEntity<?> contarReaccionesDePelicula(
            @PathVariable Long id,
            Authentication authentication
    ) {
        try {
            return ResponseEntity.ok(
                    peliculaReaccionService.contarReaccionesDePelicula(id, authentication)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/mis-reacciones")
    public ResponseEntity<?> obtenerMisReacciones(Authentication authentication) {
        try {
            return ResponseEntity.ok(
                    peliculaReaccionService.obtenerMisReacciones(authentication)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/mis-reacciones/{tipoReaccion}")
    public ResponseEntity<?> obtenerPeliculasPorReaccion(
            @PathVariable String tipoReaccion,
            Authentication authentication
    ) {
        try {
            return ResponseEntity.ok(
                    peliculaReaccionService.obtenerPeliculasPorReaccion(tipoReaccion, authentication)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}