package menuing.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import menuing.entity.Admin;

@Stateless
public class Admins {
    @PersistenceContext(name = "menuing")
    EntityManager em;

    public List<Admin> findAll() {
        return this.em.createNamedQuery(Admin.FIND_ALL).getResultList();
    }
    
    public Admin findById(Long id){
        return this.em.find(Admin.class, id);
    }

    public List<Admin> findByUsername(String username) {
        Query query = this.em.createQuery("select a from Admin a where a.username = :username");
        query.setParameter("username", username);
        return query.getResultList();
    }

    public void create(Admin admin) {
        this.em.persist(admin);
    }

    public void remove(Admin admin) {
        this.em.remove(admin);
    }
}
