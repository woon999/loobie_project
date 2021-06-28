package toyproject.loobie.domain.economic;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class EconomicRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Economic economic){
        em.persist(economic);
        return economic.getId();
    }

    public Economic find(Long id){
        return em.find(Economic.class, id);
    }

}
