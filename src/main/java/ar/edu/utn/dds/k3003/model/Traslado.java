package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoTrasladoEnum;
import net.bytebuddy.asm.Advice;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "traslado")
public class Traslado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NotNull
    private String qrVianda;
    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private EstadoTrasladoEnum status;
    @Column
    @NotNull
    private LocalDateTime fechaTraslado;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ruta_id", nullable = false)
    private Ruta ruta;
    @Column
    @NotNull
    private Long colaboradorId;


    public Traslado(String qrVianda, EstadoTrasladoEnum status, LocalDateTime fechaTraslado, Ruta ruta) {
        this.qrVianda = qrVianda;
        this.status = status;
        this.fechaTraslado = fechaTraslado;
        this.ruta = ruta;
        this.colaboradorId = ruta.getColaboradorId();
    }

    public Traslado() {

    }

    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQrVianda() {
        return qrVianda;
    }

    public void setQrVianda(String qrVianda) {
        this.qrVianda = qrVianda;
    }

    public EstadoTrasladoEnum getStatus() {
        return status;
    }

    public void setStatus(EstadoTrasladoEnum status) {
        this.status = status;
    }

    public LocalDateTime getFechaTraslado() {
        return fechaTraslado;
    }

    public void setFechaTraslado(LocalDateTime fechaTraslado) {
        this.fechaTraslado = fechaTraslado;
    }


    public Long getColaboradorId() {
        return colaboradorId;
    }

    public void setColaboradorId(Long colaboradorId) {
        this.colaboradorId = colaboradorId;
    }
}
