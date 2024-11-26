package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.Clientes.ColaboradoresRetrofitClient;
import ar.edu.utn.dds.k3003.facades.dtos.TrasladoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.HttpStatus;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.NoSuchElementException;

public class ColaboradoresProxy{

    private final String endpoint;
    private final ColaboradoresRetrofitClient service;

    public ColaboradoresProxy(ObjectMapper objectMapper) {
        // agregarSuscriptor(Long colaborador_id, Integer heladera_id, int cantMinima, int viandasDisponibles, boolean notificarDesperfecto){
        var env = System.getenv();
        this.endpoint = env.getOrDefault("URL_COLABORADORES", "http://localhost:8082/");

        var retrofit =
                new Retrofit.Builder()
                        .baseUrl(this.endpoint)
                        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                        .build();

        this.service = retrofit.create(ColaboradoresRetrofitClient.class);
    }

    @SneakyThrows
    public void notificar(TrasladoDTO trasladoDTO){
        Response<Void> execute = service.notificar(trasladoDTO).execute();

        if (execute.isSuccessful()) {
            execute.body();
        }
        if (execute.code() == HttpStatus.BAD_REQUEST.getCode()) {
            throw new NoSuchElementException("No se pudo enviar la notificacion");
        }
    }

}

