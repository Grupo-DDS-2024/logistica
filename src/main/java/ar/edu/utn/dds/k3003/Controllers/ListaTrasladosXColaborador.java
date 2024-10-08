package ar.edu.utn.dds.k3003.Controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.TrasladoDTO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListaTrasladosXColaborador implements Handler {
    private  Fachada fachada;
    public ListaTrasladosXColaborador(Fachada fachada) {
        this.fachada=fachada;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        Long colaboradorId = Long.parseLong(context.pathParam("colaboradorId"));
        Integer mes = Integer.parseInt(context.queryParam("mes"));
        Integer anio = Integer.parseInt(context.queryParam("anio"));
        List<TrasladoDTO> traslados = fachada.trasladosDeColaborador(colaboradorId,mes,anio);
        context.json(traslados);
    }
}
