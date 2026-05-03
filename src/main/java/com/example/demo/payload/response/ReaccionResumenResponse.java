package com.example.demo.payload.response;

// PATRÓN DTO:
// Este objeto representa el resumen estadístico de una reacción.
// Se usará en la pantalla "Mis reacciones".
public class ReaccionResumenResponse {

    private String tipoReaccion;
    private String nombre;
    private Long total;

    public ReaccionResumenResponse() {
    }

    public ReaccionResumenResponse(String tipoReaccion, String nombre, Long total) {
        this.tipoReaccion = tipoReaccion;
        this.nombre = nombre;
        this.total = total;
    }

    public String getTipoReaccion() {
        return tipoReaccion;
    }

    public void setTipoReaccion(String tipoReaccion) {
        this.tipoReaccion = tipoReaccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}