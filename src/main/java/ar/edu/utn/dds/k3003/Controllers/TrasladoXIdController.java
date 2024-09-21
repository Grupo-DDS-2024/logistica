package ar.edu.utn.dds.k3003.Controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.TrasladoDTO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class TrasladoXIdController implements Handler {
    private Fachada fachada;
    public TrasladoXIdController(Fachada fachada) {
        this.fachada=fachada;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String trasladoIdStr = context.pathParam("trasladoId");
        TrasladoDTO traslado = fachada.buscarXId(Long.parseLong(trasladoIdStr));
        context.json(traslado);


    }
}
