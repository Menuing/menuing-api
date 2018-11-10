package menuing.boundary;

import menuing.entity.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


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

    public User findByUsername(String username) {
        return this.em.find(User.class, username);
    }

    public void create(User user) {
        this.em.persist(user);
    }

    public void remove(User user) {
        this.em.remove(user);
    }
}
