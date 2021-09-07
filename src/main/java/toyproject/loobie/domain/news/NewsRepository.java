package toyproject.loobie.domain.news;

import org.springframework.stereotype.Repository;

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

    public Long saveWithDate(News news, String date){
        news.setDate(date);
        em.persist(news);
        return news.getId();
    }

    public News findOne(Long id){
        return em.find(News.class, id);
    }

    public List<News> findByDate(String date){
        return em.createQuery("select n from News n where n.date = :date", News.class)
                .setParameter("date", date)
                .getResultList();
    }

    public List<News> findAll(){
        return em.createQuery("select n from News n", News.class)
                .getResultList();
    }

    public void clear(){
        em.clear();
    }
}
