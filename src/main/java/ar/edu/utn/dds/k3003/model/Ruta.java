package ar.edu.utn.dds.k3003.model;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "ruta")
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NotNull
    private Long colaboradorId;
    @Column
    @NotNull
    private Integer heladeraOrigen;
    @Column
    @NotNull
    private Integer heladeraDestino;

     public Ruta(Long colaboradorId, Integer heladeraOrigen, Integer heladeraDestino) {
        this.colaboradorId = colaboradorId;
        this.heladeraOrigen = heladeraOrigen;
        this.heladeraDestino = heladeraDestino;
    }

    public Ruta(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getColaboradorId() {
        return colaboradorId;
    }

    public void setColaboradorId(Long colaboradorId) {
        this.colaboradorId = colaboradorId;
    }

    public Integer getHeladeraOrigen() {
        return heladeraOrigen;
    }

    public void setHeladeraOrigen(Integer heladeraOrigen) {
        this.heladeraOrigen = heladeraOrigen;
    }

    public Integer getHeladeraDestino() {
        return heladeraDestino;
    }

    public void setHeladeraDestino(Integer heladeraDestino) {
        this.heladeraDestino = heladeraDestino;
    }
}

