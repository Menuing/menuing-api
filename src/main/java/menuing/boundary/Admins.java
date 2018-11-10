package menuing.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import menuing.entity.Admin;

@Stateless
public class Admins {
    @PersistenceContext(name = "menuing")
    EntityManager em;

    public List<Admin> findAll() {
        return this.em.createNamedQuery(Admin.FIND_ALL).getResultList();
    }

    public Admin findByUsername(String username) {
        return this.em.find(Admin.class, username);
    }

    public void create(Admin admin) {
        this.em.persist(admin);
    }

    public void remove(Admin admin) {
        this.em.remove(admin);
    }
}
