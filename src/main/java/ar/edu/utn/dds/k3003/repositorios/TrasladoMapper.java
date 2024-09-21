package ar.edu.utn.dds.k3003.repositorios;

import ar.edu.utn.dds.k3003.facades.dtos.TrasladoDTO;
import ar.edu.utn.dds.k3003.model.Traslado;

public class TrasladoMapper {
    public TrasladoDTO mapear(Traslado traslado){
        TrasladoDTO trasladoDTO = new TrasladoDTO(traslado.getQrVianda(),
                                                  traslado.getStatus(),
                                                  traslado.getFechaTraslado(),
                                                  traslado.getRuta().getHeladeraOrigen(),
                                                  traslado.getRuta().getHeladeraDestino());
        trasladoDTO.setId(traslado.getId());
        trasladoDTO.setColaboradorId(traslado.getRuta().getColaboradorId());
        return trasladoDTO;
    }
}
