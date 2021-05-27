package toyproject.loobie;

import org.springframework.stereotype.Repository;
import toyproject.loobie.domain.News;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class NewsRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(News news){
        em.persist(news);
        return news.getId();
    }

    public News findOne(Long id){
        return em.find(News.class, id);
    }

}
