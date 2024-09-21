package ar.edu.utn.dds.k3003.Controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class ListaRutasController implements Handler {
    private Fachada fachada;

    public ListaRutasController(Fachada fachada){
        this.fachada=fachada;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
            context.json(fachada.rutas());
    }
}
