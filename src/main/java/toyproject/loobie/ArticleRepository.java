package toyproject.loobie;

import org.springframework.stereotype.Repository;
import toyproject.loobie.domain.Article;
import toyproject.loobie.domain.News;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ArticleRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Article article){
        em.persist(article);
        return article.getId();
    }

    public Article find(Long id){
        return em.find(Article.class, id);
    }

}
