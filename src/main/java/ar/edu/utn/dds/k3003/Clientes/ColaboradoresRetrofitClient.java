package ar.edu.utn.dds.k3003.Clientes;

import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;
import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
import ar.edu.utn.dds.k3003.facades.dtos.TrasladoDTO;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.http.*;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import retrofit2.Call;

import java.util.Map;

public interface ColaboradoresRetrofitClient {

    @POST("notificarTraslado")
    Call<Void> notificar(@Body TrasladoDTO trasladoDTO);
}
