package ar.edu.utn.dds.k3003.repositorios;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoTrasladoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.TrasladoDTO;
import ar.edu.utn.dds.k3003.model.Traslado;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class RepositorioTraslado {
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    private EntityManager entityManager;

    public RepositorioTraslado(EntityManagerFactory entityManagerFactory){
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    public RepositorioTraslado(){

    }

    public Traslado guardar(Traslado traslado){
        if(Objects.isNull(traslado.getId())){
            entityManager.persist(traslado);
        }
        return traslado;
    }

    public Traslado buscarXId(Long id){
        Traslado traslado = entityManager.find(Traslado.class,id);
        if(traslado == null){
            throw new NoSuchElementException("El traslado con id:" + id + " no existe");
        }
        return traslado;
    }

    public List<Traslado> buscarXColaborador (Long colaboradorId){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Traslado> cq = cb.createQuery(Traslado.class);
        Root<Traslado> traslado = cq.from(Traslado.class);

        Predicate predicadoColabId = cb.equal(traslado.get("colaboradorId"),colaboradorId);
        cq.where(predicadoColabId);
        List<Traslado> traslColab  = entityManager.createQuery(cq).getResultList();
        if(traslColab.isEmpty()){
            throw new NoSuchElementException("El colaborador con Id:"+ colaboradorId +"No tiene ningun traslado asignado");
        }
        return traslColab;
    }

    public List<Traslado> todos(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Traslado> cq = cb.createQuery(Traslado.class);
        Root<Traslado> root = cq.from(Traslado.class);
        cq.select(root);
        return entityManager.createQuery(cq).getResultList();
    }

    public void modificarEstadoXID(Long trasladoId, EstadoTrasladoEnum nuevoEstado) throws NoSuchElementException {
        Traslado trasladoAModificar = this.buscarXId(trasladoId);
        trasladoAModificar.setStatus(nuevoEstado);
        entityManager.merge(trasladoAModificar);
    }


}
