package menuing.boundary;

import menuing.entity.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import javax.persistence.Query;


@Stateless
public class Users {
    @PersistenceContext(name = "menuing")
    EntityManager em;

    public List<User> findAll() {
        return this.em.createNamedQuery(User.FIND_ALL).getResultList();
    }
    
    public User findById(Long id){
        return this.em.find(User.class, id);
    }

    public List<User> findByUsername(String username) {
        Query query = this.em.createQuery("select u from User u where u.username = :username");
        query.setParameter("username", username);
        return query.getResultList();
    }

    public void create(User user) {
        this.em.persist(user);
    }

    public void remove(Long id) {
        User user = findById(id);
        this.em.remove(user);
    }
}
