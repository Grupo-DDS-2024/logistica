package ar.edu.utn.dds.k3003.Controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

public class DBController implements Handler {

    private Fachada fachada;

    public DBController(Fachada fachada) {
        this.fachada = fachada;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        try {
            fachada.clear();
            context.result("Se borraron todos los datos");
        } catch (Exception e) {
            e.printStackTrace();
            context.result("Ha ocurrido un error al intentar borrar los datos");
            context.status(500);
        }
    }
}

