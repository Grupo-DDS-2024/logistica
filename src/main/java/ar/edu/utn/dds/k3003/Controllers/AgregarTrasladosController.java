package ar.edu.utn.dds.k3003.Controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.TrasladoDTO;
import ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException;
import ar.edu.utn.dds.k3003.model.Traslado;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public class AgregarTrasladosController implements Handler {
    private Fachada fachada;
    private StepMeterRegistry meterRegistry;
    private Counter contadorTraslados;

    public AgregarTrasladosController(Fachada fachada, StepMeterRegistry meterRegistry) {
        this.fachada = fachada;
        this.meterRegistry = meterRegistry;
        this.contadorTraslados = meterRegistry.counter("ddsLogistica.trasladosIniciados");
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        try {
            TrasladoDTO traslado = context.bodyAsClass(TrasladoDTO.class);
            TrasladoDTO trasladofix = new TrasladoDTO(traslado.getQrVianda(), traslado.getHeladeraOrigen(), traslado.getHeladeraDestino());
            TrasladoDTO trasladoasignado = fachada.asignarTraslado(trasladofix);
            contadorTraslados.increment();
            context.json(trasladoasignado);
        } catch (TrasladoNoAsignableException | NoSuchElementException e) {
            context.result(e.getLocalizedMessage());
            context.status(HttpStatus.BAD_REQUEST);
        }
    }
}
