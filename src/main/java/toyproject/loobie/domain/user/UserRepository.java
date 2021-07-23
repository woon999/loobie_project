package toyproject.loobie.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
@Slf4j
@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager em;

    public User save(User user){
        em.persist(user);
        return user;
    }

    public User findOne(Long id){
        return em.find(User.class, id);
    }

    public Optional<User> findByEmail(String email){
        Optional<User> user = null;
        try{
            user = Optional.ofNullable(em.createQuery("select u from User u where u.email = :email", User.class)
                    .setParameter("email", email).getSingleResult());
        }catch (NoResultException e){
//            System.out.println("###" + e);
            user = Optional.empty();
        }finally {
            return user;
        }
    }

    public List<User> findAll(){
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }
}