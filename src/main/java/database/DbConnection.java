package database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.logging.Logger;

public class DbConnection {

    private DbConnection() {}
    public static final Logger LOG = Logger.getAnonymousLogger();

    private static EntityManagerFactory emf;
    public static EntityManager em;

    public static synchronized void init() {
        if (emf == null && em == null) {
            emf = Persistence.createEntityManagerFactory("thePersistenceUnit");
            em = emf.createEntityManager();
        }
        else LOG.info("emf already initialized");
    }
}
