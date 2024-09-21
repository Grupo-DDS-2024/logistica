package ar.edu.utn.dds.k3003.Clientes;

import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;
import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.http.*;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import retrofit2.Call;

import java.util.Map;

public interface HeladerasRetrofitClient {

    @POST("retiros")
    Call<Void> retirar(@Body RetiroDTO retiro);

    @POST("depositos")
    Call<Void> depositar(@Body ViandaDTO vianda);


}
