package ar.edu.utn.dds.k3003.Controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.RutaDTO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

public class AgregarRutasController implements Handler {

    private Fachada fachada;

    public AgregarRutasController(Fachada fachada){
        this.fachada = fachada;
    }


    @Override
    public void handle(@NotNull Context context) throws Exception {
        RutaDTO rutaDTO = context.bodyAsClass(RutaDTO.class);
        var rutaDTOrta = fachada.agregar(rutaDTO);
        context.json(rutaDTOrta);
        context.status(HttpStatus.CREATED);

    }
}
