package test;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.FachadaHeladeras;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FachadaTest {
    @Mock FachadaViandas fachadaViandas;
    @Mock FachadaHeladeras fachadaHeladeras;
    Fachada fachada;
    @BeforeEach
    void setUp(){
        fachada = new Fachada();
        fachada.setViandasProxy(fachadaViandas);
        fachada.setHeladerasProxy(fachadaHeladeras);

    }

    @Test
    void testAgregarRuta(){
        RutaDTO rutaDTO = new RutaDTO(1L,1,2);
        RutaDTO rutaResultado = fachada.agregar(rutaDTO);
        assertNotNull(rutaResultado.getId(), "La ruta cuando se agrega tiene que tener un ID asignado");
        assertNotNull(rutaResultado);
        assertEquals(1L, rutaResultado.getColaboradorId());
        assertEquals(1, rutaResultado.getHeladeraIdOrigen());
        assertEquals(2, rutaResultado.getHeladeraIdDestino());
    }

    @Test
    void testAsignarTraslado() throws TrasladoNoAsignableException {
        ViandaDTO vianda = new ViandaDTO("QR",LocalDateTime.now(), EstadoViandaEnum.PREPARADA, 1L, 1);
        when(fachadaViandas.buscarXQR("QR")).thenReturn(vianda);
        RutaDTO rutaDTO = new RutaDTO(2L,1,2);
        RutaDTO rutaRes = fachada.agregar(rutaDTO);
        assertNotNull(rutaRes.getId(), "La ruta cuando se agrega tiene que tener un ID asignado");
        TrasladoDTO trasladoDTO = new TrasladoDTO("QR",1,2);
        TrasladoDTO trasladoRes = fachada.asignarTraslado(trasladoDTO);
        assertEquals(EstadoTrasladoEnum.ASIGNADO, trasladoRes.getStatus(), "El traslado no cambió de estado tras ser asignado");
        assertEquals(2L, trasladoRes.getColaboradorId(), "No se asigno al colaborador correcto");
    }

    @Test
    void testTrasladosColaborador() throws TrasladoNoAsignableException {
        ViandaDTO vianda1 = new ViandaDTO("QR1", LocalDateTime.now(), EstadoViandaEnum.PREPARADA, 1L, 1);
        ViandaDTO vianda2 = new ViandaDTO("QR2", LocalDateTime.now(), EstadoViandaEnum.PREPARADA, 1L, 3);
        ViandaDTO vianda3 = new ViandaDTO("QR3", LocalDateTime.now(), EstadoViandaEnum.PREPARADA, 2L, 5);
        RutaDTO ruta1 = new RutaDTO(1L, 1, 2);
        RutaDTO ruta2 = new RutaDTO(1L, 3, 4);
        RutaDTO ruta3 = new RutaDTO(2L, 5, 6);
        fachada.agregar(ruta1); fachada.agregar(ruta2); fachada.agregar(ruta3);
        TrasladoDTO traslado1 = new TrasladoDTO("QR1",1,2);
        TrasladoDTO traslado2 = new TrasladoDTO("QR2",3,4);
        TrasladoDTO traslado3 = new TrasladoDTO("QR3",5,6);
        when(fachadaViandas.buscarXQR("QR1")).thenReturn(vianda1);
        when(fachadaViandas.buscarXQR("QR2")).thenReturn(vianda2);
        when(fachadaViandas.buscarXQR("QR3")).thenReturn(vianda3);
        fachada.asignarTraslado(traslado1);
        fachada.asignarTraslado(traslado2);
        fachada.asignarTraslado(traslado3);
        assertNotNull(fachada.trasladosDeColaborador(1L, LocalDateTime.now().getMonthValue(),LocalDateTime.now().getYear()));
        assertEquals(2, fachada.trasladosDeColaborador(1L, LocalDateTime.now().getMonthValue(),LocalDateTime.now().getYear()).size());
        assertEquals(1,fachada.trasladosDeColaborador(2L, LocalDateTime.now().getMonthValue(),LocalDateTime.now().getYear()).size());
    }

    @Test
    void testTrasladoRetirado() throws TrasladoNoAsignableException {
        ViandaDTO vianda = new ViandaDTO("QR",LocalDateTime.now(), EstadoViandaEnum.PREPARADA, 1L, 1);
        when(fachadaViandas.buscarXQR("QR")).thenReturn(vianda);
        fachada.agregar(new RutaDTO(1L,1,2));
        TrasladoDTO traslado = fachada.asignarTraslado(new TrasladoDTO("QR",1,2));
        fachada.trasladoRetirado(traslado.getId());
        RetiroDTO retiro = new RetiroDTO(traslado.getQrVianda(),"321",traslado.getHeladeraOrigen());
        verify(fachadaHeladeras, times(1)).retirar(retiro);
        verify(fachadaViandas, times(1)).modificarEstado(traslado.getQrVianda(), EstadoViandaEnum.EN_TRASLADO);

    }
    @Test
    void testTrasladoDepositado() throws TrasladoNoAsignableException {
        ViandaDTO vianda = new ViandaDTO("QR",LocalDateTime.now(), EstadoViandaEnum.PREPARADA, 1L, 1);
        when(fachadaViandas.buscarXQR("QR")).thenReturn(vianda);
        fachada.agregar(new RutaDTO(1L,1,2));
        TrasladoDTO traslado = fachada.asignarTraslado(new TrasladoDTO("QR",1,2));
        fachada.trasladoDepositado(traslado.getId());
        //RetiroDTO retiro = new RetiroDTO(traslado.getQrVianda(),"321",traslado.getHeladeraOrigen());
        verify(fachadaHeladeras, times(1)).depositar(traslado.getHeladeraDestino(), traslado.getQrVianda());
        verify(fachadaViandas, times(1)).modificarEstado(traslado.getQrVianda(), EstadoViandaEnum.DEPOSITADA);
        verify(fachadaViandas,times(1)).modificarHeladera(traslado.getQrVianda(), traslado.getHeladeraDestino());
    }
    @Test
    void TrasladoNoAsignable() {
        fachada.agregar(new RutaDTO(1L, 1, 2));
        var traslado = new TrasladoDTO("Prueba", 1, 3);
        assertThrows(TrasladoNoAsignableException.class,
                () -> fachada.asignarTraslado(traslado));
    }
}