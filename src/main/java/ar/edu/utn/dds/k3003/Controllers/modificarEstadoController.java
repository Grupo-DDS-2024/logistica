package ar.edu.utn.dds.k3003.Controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoTrasladoEnum;
import ar.edu.utn.dds.k3003.repositorios.RepositorioTraslado;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class modificarEstadoController implements Handler {
    private Fachada fachada;
    public modificarEstadoController(Fachada fachada) {
        this.fachada = fachada;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String trasladoIdStr = context.pathParam("trasladoId");
        Long trasladoId = Long.parseLong(trasladoIdStr);
        EstadoTrasladoEnum nuevoEstado = context.bodyAsClass(EstadoTrasladoEnum.class);
        fachada.modificarEstadoTraslado(trasladoId, nuevoEstado);
        context.result("Estado modificado correctamente");
    }
}
