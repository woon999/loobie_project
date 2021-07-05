package toyproject.loobie.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
}
//public class UserRepository {
//
//    @PersistenceContext
//    private EntityManager em;
//
//    public User save(User user){
//        em.persist(user);
//        return user;
//    }
//
//    public User findOne(Long id){
//        return em.find(User.class, id);
//    }
//
//    public Optional<User> findByEmail(String email){
//        return Optional.ofNullable(em.createQuery("select u from User u where u.email = :email", User.class)
//                .setParameter("email", email).getSingleResult());
//    }
//
//    public List<User> findAll(){
//        return em.createQuery("select u from User u", User.class)
//                .getResultList();
//    }
//
//
//}
