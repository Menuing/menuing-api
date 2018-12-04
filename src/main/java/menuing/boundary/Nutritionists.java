package menuing.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import menuing.entity.Nutritionist;
import menuing.entity.User;

@Stateless
public class Nutritionists {
    @PersistenceContext(name = "menuing")
    EntityManager em;

    public List<Nutritionist> findAll() {
        return this.em.createNamedQuery(Nutritionist.FIND_ALL).getResultList();
    }
    
    public Nutritionist findById(Long id){
        return this.em.find(Nutritionist.class, id);
    }

    public List<Nutritionist> findByName(String name) {
        Query query = this.em.createQuery("select u from Nutritionist u where u.username = :name");
        query.setParameter("name", name);
        return query.getResultList();
    }
    
    public List<Nutritionist> findByNameLike(String name) {
        Query query = this.em.createQuery("select u from Nutritionist u where u.username LIKE :name");
        query.setParameter("name", "%"+name+"%");
        return query.setMaxResults(10).getResultList();
    }

    public void create(Nutritionist nutritionist) {
        this.em.persist(nutritionist);
    }

    public void remove(Long id) {
        Nutritionist nutritionist = findById(id);
        this.em.remove(nutritionist);
    }
}
