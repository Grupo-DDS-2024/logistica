package ar.edu.utn.dds.k3003.repositorios;

import ar.edu.utn.dds.k3003.facades.dtos.RutaDTO;
import ar.edu.utn.dds.k3003.model.Ruta;

public class RutaMapper {
    public RutaDTO mapear(Ruta ruta){
        RutaDTO rutaDTO = new RutaDTO(ruta.getColaboradorId(),
                                      ruta.getHeladeraOrigen(),
                                      ruta.getHeladeraDestino());
        rutaDTO.setId(ruta.getId());
        return rutaDTO;
    }
}
