package toyproject.loobie;

import org.springframework.stereotype.Repository;
import toyproject.loobie.domain.News;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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

    public List<News> findAll(){
        return em.createQuery("select n from News n", News.class)
                .getResultList();
    }

}
