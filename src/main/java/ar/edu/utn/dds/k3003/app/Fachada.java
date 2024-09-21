package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaHeladeras;
import ar.edu.utn.dds.k3003.facades.FachadaLogistica;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException;
import ar.edu.utn.dds.k3003.model.Ruta;
import ar.edu.utn.dds.k3003.model.Traslado;
import ar.edu.utn.dds.k3003.repositorios.RepositorioRuta;
import ar.edu.utn.dds.k3003.repositorios.RepositorioTraslado;
import ar.edu.utn.dds.k3003.repositorios.RutaMapper;
import ar.edu.utn.dds.k3003.repositorios.TrasladoMapper;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class Fachada implements FachadaLogistica {
    private EntityManagerFactory entityManagerFactory;
    private final RepositorioRuta repositorioRuta;
    private final RepositorioTraslado repositorioTraslado;
    private final RutaMapper rutaMapper;
    private final TrasladoMapper trasladoMapper;
    private FachadaViandas fachadaViandas;
    private FachadaHeladeras fachadaHeladeras;

    public Fachada(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
        this.repositorioRuta = new RepositorioRuta();
        this.rutaMapper = new RutaMapper();
        this.repositorioTraslado = new RepositorioTraslado();
        this.trasladoMapper = new TrasladoMapper();
    }
    public Fachada(){
        this.repositorioRuta = new RepositorioRuta();
        this.rutaMapper = new RutaMapper();
        this.repositorioTraslado = new RepositorioTraslado();
        this.trasladoMapper = new TrasladoMapper();
    }
@Override
public RutaDTO agregar(RutaDTO rutaDTO){
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    repositorioRuta.setEntityManager(entityManager);
    repositorioRuta.getEntityManager().getTransaction().begin();
    Ruta ruta = new Ruta(rutaDTO.getColaboradorId(), rutaDTO.getHeladeraIdOrigen(), rutaDTO.getHeladeraIdDestino());
    ruta = this.repositorioRuta.guardar(ruta);
    repositorioRuta.getEntityManager().getTransaction().commit();
    repositorioRuta.getEntityManager().close();
    return rutaMapper.mapear(ruta);
}

@Override
    public TrasladoDTO buscarXId(Long trasladoID) throws NoSuchElementException{
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    repositorioTraslado.setEntityManager(entityManager);
        repositorioTraslado.getEntityManager().getTransaction().begin();
        TrasladoDTO traslado = trasladoMapper.mapear(repositorioTraslado.buscarXId(trasladoID));
        repositorioTraslado.getEntityManager().getTransaction().commit();
    repositorioTraslado.getEntityManager().close();
        return  traslado;
}

    public  RutaDTO buscarRutaXId(Long rutaID) throws NoSuchElementException{
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        repositorioTraslado.setEntityManager(entityManager);
        repositorioRuta.getEntityManager().getTransaction().begin();
        RutaDTO ruta = rutaMapper.mapear(repositorioRuta.buscarXId(rutaID));
        repositorioRuta.getEntityManager().getTransaction().commit();
        repositorioRuta.getEntityManager().close();
        return ruta;
    }


@Override
    public TrasladoDTO asignarTraslado(TrasladoDTO trasladoDTO) throws TrasladoNoAsignableException{
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    repositorioTraslado.setEntityManager(entityManager);
    repositorioRuta.setEntityManager(entityManager);
    repositorioTraslado.getEntityManager().getTransaction().begin();
    ViandaDTO viandaDTO = fachadaViandas.buscarXQR(trasladoDTO.getQrVianda());
    Random random = new Random();
    List<Ruta> rutasPosibles = repositorioRuta.buscarXHeladera(trasladoDTO.getHeladeraOrigen(), trasladoDTO.getHeladeraDestino());
    if(rutasPosibles.isEmpty()){
        entityManager.getTransaction().rollback();
        entityManager.close();
        throw new TrasladoNoAsignableException("No se encontraron rutas posibles para el traslado");
    }
    Ruta ruta = rutasPosibles.get(random.nextInt(rutasPosibles.size()));
    Traslado traslado = new Traslado(viandaDTO.getCodigoQR(), EstadoTrasladoEnum.ASIGNADO, trasladoDTO.getFechaTraslado(), ruta);
    repositorioTraslado.guardar(traslado);
    repositorioTraslado.getEntityManager().getTransaction().commit();
    repositorioTraslado.getEntityManager().close();
    repositorioRuta.getEntityManager().close();
    return trasladoMapper.mapear(traslado);

    }


    public List<TrasladoDTO> findByColaboradorId(Long colaboradorId){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        repositorioTraslado.setEntityManager(entityManager);
        repositorioTraslado.getEntityManager().getTransaction().begin();
        List<Traslado> traslados = repositorioTraslado.buscarXColaborador(colaboradorId);
        repositorioTraslado.getEntityManager().getTransaction().commit();
        repositorioTraslado.getEntityManager().close();
        return traslados.stream().map(trasladoMapper::mapear).toList();
    }
@Override
    public List<TrasladoDTO> trasladosDeColaborador(Long colaboradorId, Integer mes, Integer anio){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        repositorioTraslado.setEntityManager(entityManager);
        repositorioTraslado.getEntityManager().getTransaction().begin();
        List<Traslado> traslados = repositorioTraslado.buscarXColaborador(colaboradorId);
        List<Traslado> trasladosFiltrados = traslados.stream().filter(t -> t.getFechaTraslado().getMonthValue() == mes &&
                                                                        t.getFechaTraslado().getYear() == anio).toList();
        repositorioTraslado.getEntityManager().getTransaction().commit();
        repositorioTraslado.getEntityManager().close();
        return trasladosFiltrados.stream().map(trasladoMapper :: mapear).toList();
}

@Override
    public void trasladoRetirado(Long trasladoId){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        repositorioTraslado.setEntityManager(entityManager);
        repositorioTraslado.getEntityManager().getTransaction().begin();
        Traslado traslado = repositorioTraslado.buscarXId(trasladoId);
        RetiroDTO retiro = new RetiroDTO(traslado.getQrVianda(), "321", traslado.getRuta().getHeladeraOrigen());
        fachadaHeladeras.retirar(retiro);
        //fachadaViandas.modificarEstado(traslado.getQrVianda(), EstadoViandaEnum.EN_TRASLADO);
        repositorioTraslado.getEntityManager().getTransaction().commit();
        repositorioTraslado.getEntityManager().close();
        this.modificarEstadoTraslado(trasladoId,EstadoTrasladoEnum.EN_VIAJE);
}

@Override
    public void trasladoDepositado(Long trasladoId){
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    repositorioTraslado.setEntityManager(entityManager);
    repositorioTraslado.getEntityManager().getTransaction().begin();
    TrasladoDTO traslado = trasladoMapper.mapear(repositorioTraslado.buscarXId(trasladoId));
    if(!traslado.getStatus().equals(EstadoTrasladoEnum.EN_VIAJE)){
        repositorioTraslado.getEntityManager().getTransaction().rollback();
        repositorioTraslado.getEntityManager().close();
        throw new NoSuchElementException("La vianda "+traslado.getQrVianda()+" no fue retirada");
    }
    fachadaHeladeras.depositar(traslado.getHeladeraDestino(), traslado.getQrVianda());
    fachadaViandas.modificarHeladera(traslado.getQrVianda(),traslado.getHeladeraDestino());
    //fachadaViandas.modificarEstado(traslado.getQrVianda(), EstadoViandaEnum.DEPOSITADA);
    repositorioTraslado.getEntityManager().getTransaction().commit();
    repositorioTraslado.getEntityManager().close();
    this.modificarEstadoTraslado(trasladoId,EstadoTrasladoEnum.ENTREGADO);
}

    public List<RutaDTO> rutas(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        repositorioRuta.setEntityManager(entityManager);
        repositorioRuta.getEntityManager().getTransaction().begin();
        List<RutaDTO> rutas = repositorioRuta.todos().stream().map(rutaMapper::mapear).toList();
        repositorioRuta.getEntityManager().getTransaction().commit();
        repositorioRuta.getEntityManager().close();
        return rutas;
    }

    public List<TrasladoDTO> traslados(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        repositorioTraslado.setEntityManager(entityManager);
        repositorioTraslado.getEntityManager().getTransaction().begin();
        List<TrasladoDTO> traslados = repositorioTraslado.todos().stream().map(trasladoMapper::mapear).toList();
        repositorioTraslado.getEntityManager().getTransaction().commit();
        repositorioTraslado.getEntityManager().close();
        return traslados;
    }

    public void modificarEstadoTraslado(Long trasladoId, EstadoTrasladoEnum nuevoEstado) throws NoSuchElementException{
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        repositorioTraslado.setEntityManager(entityManager);
        repositorioTraslado.getEntityManager().getTransaction().begin();
        repositorioTraslado.modificarEstadoXID(trasladoId, nuevoEstado);
        repositorioTraslado.getEntityManager().getTransaction().commit();
        repositorioTraslado.getEntityManager().close();
    }

    public void clear(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        repositorioRuta.setEntityManager(entityManager);
        repositorioTraslado.setEntityManager(entityManager);
        entityManager.getTransaction().begin();
        try{
            entityManager.createQuery("DELETE FROM Traslado").executeUpdate();
            entityManager.createQuery("DELETE FROM Ruta ").executeUpdate();
            entityManager.getTransaction().commit();
        } catch (RuntimeException e){
            if(entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            throw e;
        }finally {
            entityManager.close();
        }
    }


@Override
public void setHeladerasProxy(FachadaHeladeras fachadaHeladeras){this.fachadaHeladeras = fachadaHeladeras;}
@Override
public void setViandasProxy(FachadaViandas fachadaViandas){this.fachadaViandas = fachadaViandas;}


    public RepositorioRuta getRepositorioRuta() {
        return repositorioRuta;
    }

    public RepositorioTraslado getRepositorioTraslado() {
        return repositorioTraslado;
    }

    public RutaMapper getRutaMapper() {
        return rutaMapper;
    }

    public TrasladoMapper getTrasladoMapper() {
        return trasladoMapper;
    }

    public FachadaViandas getFachadaViandas() {
        return fachadaViandas;
    }

    public FachadaHeladeras getFachadaHeladeras() {
        return fachadaHeladeras;
    }
}


