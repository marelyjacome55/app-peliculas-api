package com.example.demo.service;

import com.example.demo.factory.ReaccionResumenFactory;
import com.example.demo.models.Pelicula;
import com.example.demo.models.PeliculaReaccion;
import com.example.demo.models.TipoReaccion;
import com.example.demo.models.User;
import com.example.demo.payload.request.ComentarioPersonalRequest;
import com.example.demo.payload.response.PeliculaEnReaccionResponse;
import com.example.demo.payload.response.PeliculaReaccionResponse;
import com.example.demo.payload.response.ReaccionResumenResponse;
import com.example.demo.repository.PeliculaReaccionRepository;
import com.example.demo.repository.PeliculaRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// PATRÓN SERVICE LAYER:
// Esta clase concentra la lógica de negocio relacionada con comentarios personales,
// reacciones y colecciones automáticas de reacciones.
// Así evitamos poner reglas de negocio directamente en el controlador.
@Service
public class PeliculaReaccionService {

    private final PeliculaRepository peliculaRepository;
    private final PeliculaReaccionRepository peliculaReaccionRepository;
    private final UserRepository userRepository;

    public PeliculaReaccionService(PeliculaRepository peliculaRepository,
                                   PeliculaReaccionRepository peliculaReaccionRepository,
                                   UserRepository userRepository) {
        this.peliculaRepository = peliculaRepository;
        this.peliculaReaccionRepository = peliculaReaccionRepository;
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

    private Pelicula obtenerPeliculaPermitida(Long idPelicula, Authentication authentication) {
        User user = obtenerUsuarioAutenticado(authentication);

        if (esAdmin(authentication)) {
            return peliculaRepository.findById(idPelicula)
                    .orElseThrow(() -> new RuntimeException("Película no encontrada"));
        }

        return peliculaRepository.findByIdAndUser(idPelicula, user)
                .orElseThrow(() -> new RuntimeException("Película no encontrada o no te pertenece"));
    }

    private TipoReaccion convertirTipoReaccion(String tipoReaccion) {
        try {
            return TipoReaccion.valueOf(tipoReaccion.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de reacción no válido: " + tipoReaccion);
        }
    }

    public Pelicula actualizarComentarioPersonal(Long idPelicula,
                                                 ComentarioPersonalRequest request,
                                                 Authentication authentication) {
        Pelicula pelicula = obtenerPeliculaPermitida(idPelicula, authentication);
        pelicula.setComentarioPersonal(request.getComentarioPersonal());
        return peliculaRepository.save(pelicula);
    }

    public List<PeliculaReaccionResponse> obtenerReaccionesDePelicula(Long idPelicula,
                                                                      Authentication authentication) {
        Pelicula pelicula = obtenerPeliculaPermitida(idPelicula, authentication);

        return peliculaReaccionRepository.findByPelicula(pelicula)
                .stream()
                .map(reaccion -> ReaccionResumenFactory.crearReaccion(reaccion.getTipoReaccion()))
                .toList();
    }

    public List<PeliculaReaccionResponse> agregarReaccion(Long idPelicula,
                                                          String tipoReaccionTexto,
                                                          Authentication authentication) {
        Pelicula pelicula = obtenerPeliculaPermitida(idPelicula, authentication);
        TipoReaccion tipoReaccion = convertirTipoReaccion(tipoReaccionTexto);

        boolean yaExiste = peliculaReaccionRepository.existsByPeliculaAndTipoReaccion(pelicula, tipoReaccion);

        if (!yaExiste) {
            PeliculaReaccion nuevaReaccion = new PeliculaReaccion(pelicula, tipoReaccion);
            peliculaReaccionRepository.save(nuevaReaccion);
        }

        return obtenerReaccionesDePelicula(idPelicula, authentication);
    }

    public List<PeliculaReaccionResponse> eliminarReaccion(Long idPelicula,
                                                           String tipoReaccionTexto,
                                                           Authentication authentication) {
        Pelicula pelicula = obtenerPeliculaPermitida(idPelicula, authentication);
        TipoReaccion tipoReaccion = convertirTipoReaccion(tipoReaccionTexto);

        peliculaReaccionRepository.findByPeliculaAndTipoReaccion(pelicula, tipoReaccion)
                .ifPresent(peliculaReaccionRepository::delete);

        return obtenerReaccionesDePelicula(idPelicula, authentication);
    }

    public Long contarReaccionesDePelicula(Long idPelicula, Authentication authentication) {
        Pelicula pelicula = obtenerPeliculaPermitida(idPelicula, authentication);
        return peliculaReaccionRepository.countByPelicula(pelicula);
    }

    public List<ReaccionResumenResponse> obtenerMisReacciones(Authentication authentication) {
        User user = obtenerUsuarioAutenticado(authentication);

        List<ReaccionResumenResponse> resumen = new ArrayList<>();

        for (TipoReaccion tipoReaccion : TipoReaccion.values()) {
            Long total = peliculaReaccionRepository.countByPelicula_UserAndTipoReaccion(user, tipoReaccion);
            resumen.add(ReaccionResumenFactory.crearResumen(tipoReaccion, total));
        }

        return resumen;
    }

    public List<PeliculaEnReaccionResponse> obtenerPeliculasPorReaccion(String tipoReaccionTexto,
                                                                        Authentication authentication) {
        User user = obtenerUsuarioAutenticado(authentication);
        TipoReaccion tipoReaccion = convertirTipoReaccion(tipoReaccionTexto);

        return peliculaReaccionRepository.findByPelicula_UserAndTipoReaccion(user, tipoReaccion)
                .stream()
                .map(reaccion -> ReaccionResumenFactory.crearPeliculaEnReaccion(reaccion.getPelicula()))
                .toList();
    }
}