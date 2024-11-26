package ar.edu.utn.dds.k3003.Controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public class RetirarSinTraslado implements Handler {
    private Fachada fachada;

    public RetirarSinTraslado(Fachada fachada) {
        this.fachada = fachada;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {

        try {
            RetiroDTO retiroDTO = context.bodyAsClass(RetiroDTO.class);
            fachada.retirarVianda(retiroDTO.getHeladeraId(), retiroDTO.getQrVianda());
            context.result("Vianda retirada correctamente");
        } catch (NoSuchElementException e) {
            throw new BadRequestResponse(e.getMessage());
        }

    }
}
