package ar.edu.utn.dds.k3003.Controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.RutaDTO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import io.prometheus.metrics.core.metrics.Gauge;
import org.jetbrains.annotations.NotNull;

public class AgregarRutasController implements Handler {

    private Fachada fachada;
    private StepMeterRegistry stepMeterRegistry;
    private Counter contadorRutas;

    public AgregarRutasController(Fachada fachada, StepMeterRegistry stepMeterRegistry) {
        this.fachada = fachada;
        this.stepMeterRegistry = stepMeterRegistry;
        this.contadorRutas = stepMeterRegistry.counter("ddsLogistica.rutasCreadas");

        var gauge = stepMeterRegistry.gauge("ddsLogistica.CantRutasEnBD", fachada, f -> f.cantRutas());
    }


    @Override
    public void handle(@NotNull Context context) throws Exception {
        RutaDTO rutaDTO = context.bodyAsClass(RutaDTO.class);
        var rutaDTOrta = fachada.agregar(rutaDTO);
        contadorRutas.increment();
        System.out.println("Gauge value (initial): " + stepMeterRegistry.get("ddsLogistica.CantRutasEnBD").gauge().value());
        context.json(rutaDTOrta);
        context.status(HttpStatus.CREATED);

    }
}
