package ar.edu.utn.dds.k3003.Controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public class RetirarController implements Handler {
    private Fachada fachada;
    public RetirarController(Fachada fachada) {
        this.fachada = fachada;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {

        try{Long trasladoId = Long.parseLong(context.pathParam("trasladoId"));
            fachada.trasladoRetirado(trasladoId);
            context.result("Vianda retirada correctamente");
            } catch (NoSuchElementException e){
                throw  new BadRequestResponse(e.getMessage());
        }

    }
}
