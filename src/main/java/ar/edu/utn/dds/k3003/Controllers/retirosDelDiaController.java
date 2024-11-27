package ar.edu.utn.dds.k3003.Controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public class retirosDelDiaController implements Handler {
    Fachada fachada;

    public retirosDelDiaController(Fachada fachada) {
        this.fachada = fachada;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        try {
            context.result("Cantidad de retiros del dia de hoy: " + fachada.trasladosDelDia());
        }catch (NoSuchElementException e) {
            throw new BadRequestResponse(e.getMessage());
        }
    }
}
