package ar.edu.utn.dds.k3003.app;


import ar.edu.utn.dds.k3003.Clientes.HeladeraProxy;
import ar.edu.utn.dds.k3003.Clientes.ViandasProxy;
import ar.edu.utn.dds.k3003.Controllers.*;
import ar.edu.utn.dds.k3003.facades.dtos.Constants;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class WebApp {

    String URL_VIANDAS;
    String URL_LOGISTICA;
    String URL_HELADERAS;
    String URL_COLABORADORES;

    public static EntityManagerFactory entityManagerFactory;
    public static void main(String[] args) {
        startEntityManagerFactory();
        var env = System.getenv();
        Fachada fachada = new Fachada(entityManagerFactory);
        ObjectMapper objectMapper = createObjectMapper();
        fachada.setViandasProxy(new ViandasProxy(objectMapper));
        fachada.setHeladerasProxy(new HeladeraProxy(objectMapper));

        var port = Integer.parseInt(env.getOrDefault("PORT", "8080"));

        var app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson().updateMapper(mapper -> {
                configureObjectMapper(mapper);
            }));
        }).start(port);
        app.get("/", ctx -> ctx.result("HOLA MUNDO"));

        app.get("/rutas", new ListaRutasController(fachada));
        app.get("/rutas/{rutaID}", new BuscarRutaXIDController(fachada));
        app.post("/rutas", new AgregarRutasController(fachada));
        app.get("/traslados", new ListaTrasladosController(fachada));
        app.patch("/traslados/{trasladoId}", new modificarEstadoController(fachada));
        app.get("/traslados/{trasladoId}", new TrasladoXIdController(fachada));
        app.post("/traslados", new AgregarTrasladosController(fachada));
        app.post("/depositar/{trasladoId}", new DepositarCotroller(fachada));
        app.post("/retirar/{trasladoId}", new RetirarController(fachada));
        app.get("/traslados/search/{colaboradorId}", new ListaTrasladosXColaborador(fachada));
        app.get("/clear",new DBController(fachada));
    }

    public static ObjectMapper createObjectMapper() {
        var objectMapper = new ObjectMapper();
        configureObjectMapper(objectMapper);
        return objectMapper;
    }

    public static void configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        var sdf = new SimpleDateFormat(Constants.DEFAULT_SERIALIZATION_FORMAT, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        objectMapper.setDateFormat(sdf);
    }

    public static void startEntityManagerFactory() {
// https://stackoverflow.com/questions/8836834/read-environment-variables-in-persistence-xml-file
        Map<String, String> env = System.getenv();
        Map<String, Object> configOverrides = new HashMap<String, Object>();
        String[] keys = new String[] { "javax.persistence.jdbc.url", "javax.persistence.jdbc.user",
                "javax.persistence.jdbc.password", "javax.persistence.jdbc.driver", "hibernate.hbm2ddl.auto",
                "hibernate.connection.pool_size", "hibernate.show_sql" };
        for (String key : keys) {
            if (env.containsKey(key)) {
                String value = env.get(key);
                configOverrides.put(key, value);
            }
        }
         entityManagerFactory = Persistence.createEntityManagerFactory("db", configOverrides);
    }



}
