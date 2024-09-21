package ar.edu.utn.dds.k3003.Clientes;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import retrofit2.Call;
import retrofit2.http.*;

public interface ViandasRetrofitClient {

    @GET("viandas/{qr}")
    Call<ViandaDTO> get(@Path("qr") String qr);

    @PATCH("viandas/{qr}")
    Call<ViandaDTO> modificarHeladera(@Path("qr") String qr, @Query("heladeraId") int heladeraDestino);

    @PATCH("viandas/{qr}")
    Call<ViandaDTO> modificarEstado(@Path("qr") String qr, @Query("estado") EstadoViandaEnum status);


}
