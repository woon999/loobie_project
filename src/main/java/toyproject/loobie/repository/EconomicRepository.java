package toyproject.loobie.repository;

import org.springframework.stereotype.Repository;
import toyproject.loobie.domain.Article;
import toyproject.loobie.domain.Economic;

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
