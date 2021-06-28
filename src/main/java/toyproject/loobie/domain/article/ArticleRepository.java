package toyproject.loobie.domain.article;

import org.springframework.stereotype.Repository;

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
