package ar.edu.utn.dds.k3003.Controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public class DepositarSinTraslado implements Handler {
    private Fachada fachada;

    public DepositarSinTraslado(Fachada fachada) {
        this.fachada = fachada;
    }


    @Override
    public void handle(@NotNull Context context) throws Exception {
        try {
            ViandaDTO viandaDTO = context.bodyAsClass(ViandaDTO.class);
            //Long id = Long.parseLong(trasladoId);
            fachada.depositarVianda(viandaDTO.getHeladeraId(), viandaDTO.getCodigoQR());
            context.result("Vianda depositada correctamente");
        } catch (NoSuchElementException e) {
            throw new BadRequestResponse(e.getMessage());
        }


    }
}