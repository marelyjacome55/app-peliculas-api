package com.example.demo.factory;

import com.example.demo.models.Pelicula;
import com.example.demo.models.TipoReaccion;
import com.example.demo.payload.response.PeliculaEnReaccionResponse;
import com.example.demo.payload.response.PeliculaReaccionResponse;
import com.example.demo.payload.response.ReaccionResumenResponse;

// PATRÓN FACTORY METHOD:
// Esta clase centraliza la creación de objetos de respuesta relacionados con reacciones.
// Así evitamos repetir la lógica de construcción de DTOs en el servicio o controlador.
public class ReaccionResumenFactory {

    public static ReaccionResumenResponse crearResumen(TipoReaccion tipoReaccion, Long total) {
        return new ReaccionResumenResponse(
                tipoReaccion.name(),
                obtenerNombreVisible(tipoReaccion),
                total
        );
    }

    public static PeliculaReaccionResponse crearReaccion(TipoReaccion tipoReaccion) {
        return new PeliculaReaccionResponse(
                tipoReaccion.name(),
                obtenerNombreVisible(tipoReaccion)
        );
    }

    public static PeliculaEnReaccionResponse crearPeliculaEnReaccion(Pelicula pelicula) {
        return new PeliculaEnReaccionResponse(
                pelicula.getId(),
                pelicula.getNombre(),
                pelicula.getPortada(),
                pelicula.getGenero(),
                pelicula.getCalificacion(),
                pelicula.getVista(),
                pelicula.getComentarioPersonal()
        );
    }

    public static String obtenerNombreVisible(TipoReaccion tipoReaccion) {
        switch (tipoReaccion) {
            case ME_GUSTA:
                return "Me gusta";
            case NO_ME_GUSTA:
                return "No me gusta";
            case ME_ENCANTA:
                return "Me encanta";
            case ME_ABURRIO:
                return "Me aburrió";
            case ME_HIZO_REIR:
                return "Me hizo reír";
            case ME_SORPRENDIO:
                return "Me sorprendió";
            default:
                return tipoReaccion.name();
        }
    }
}