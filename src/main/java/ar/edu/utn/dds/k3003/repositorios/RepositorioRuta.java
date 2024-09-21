package ar.edu.utn.dds.k3003.repositorios;

import ar.edu.utn.dds.k3003.model.Ruta;
import org.hibernate.tuple.entity.EntityMetamodel;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class RepositorioRuta {
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    private EntityManager entityManager;
    public RepositorioRuta(EntityManagerFactory entityManagerFactory){
        this.entityManager = entityManagerFactory.createEntityManager();
    }
    public RepositorioRuta(){
    }

    public Ruta guardar(Ruta ruta){
        if(Objects.isNull(ruta.getId())){
            this.entityManager.persist(ruta);
        }
        return ruta;
    }

    public Ruta buscarXId(Long id){
        Ruta ruta = entityManager.find(Ruta.class,id);
        if(ruta == null){
            throw  new NoSuchElementException("La ruta con id:" + id + " no existe");
        }
        return ruta;
    }

//    public List<Ruta> buscarXHeladera(Integer heladeraOrigen, Integer heladeraDestino){
//        return rutas.stream().filter(r -> r.getHeladeraOrigen().equals(heladeraOrigen) &&
//                r.getHeladeraDestino().equals(heladeraDestino)).toList();
//    }

    public List<Ruta> buscarXHeladera(Integer heladeraOrigen, Integer heladeraDestino){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ruta> cq = cb.createQuery(Ruta.class);
        Root<Ruta> ruta = cq.from(Ruta.class);

        Predicate predicadoHeladeraOrig = cb.equal(ruta.get("heladeraOrigen"), heladeraOrigen);
        Predicate predicadoHeladeraDest = cb.equal(ruta.get("heladeraDestino"), heladeraDestino);

        cq.where(cb.and(predicadoHeladeraOrig,predicadoHeladeraDest));
        return entityManager.createQuery(cq).getResultList();
    }

    public List<Ruta> todos(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ruta> cq = cb.createQuery(Ruta.class);
        Root<Ruta> ruta = cq.from(Ruta.class);
        cq.select(ruta);
        return entityManager.createQuery(cq).getResultList();
    }
}
