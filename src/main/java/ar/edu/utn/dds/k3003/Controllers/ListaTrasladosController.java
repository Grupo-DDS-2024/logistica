package ar.edu.utn.dds.k3003.Controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.repositorios.RepositorioTraslado;
import ar.edu.utn.dds.k3003.repositorios.TrasladoMapper;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class ListaTrasladosController implements Handler {
    private Fachada fachada;
    public ListaTrasladosController(Fachada fachada) {
        this.fachada=fachada;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        context.json(fachada.traslados());
    }
}
