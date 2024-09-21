package ar.edu.utn.dds.k3003.Controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.RutaDTO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class BuscarRutaXIDController implements Handler {

    private Fachada fachada;

    public BuscarRutaXIDController(Fachada fachada) {
        this.fachada = fachada;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        String rutaIdStr = context.pathParam("rutaID");
        RutaDTO ruta = fachada.buscarRutaXId(Long.parseLong(rutaIdStr));
        context.json(ruta);
    }
}
