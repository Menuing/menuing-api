package menuing.boundary;

import menuing.entity.GuestBook;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Stateless
public class Message {
    @PersistenceContext(name = "GuestBookPU")
    EntityManager em;

    public List<GuestBook> findAll() {
        return this.em.createNamedQuery(GuestBook.FIND_ALL).getResultList();
    }

    public GuestBook findById(Long id) {
        return this.em.find(GuestBook.class, id);
    }

    public void create(GuestBook guestBook) {
        this.em.persist(guestBook);
    }

    public void remove(GuestBook guestBook) {
        this.em.remove(guestBook);
    }
}
