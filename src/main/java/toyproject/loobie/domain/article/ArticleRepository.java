package toyproject.loobie.domain.article;

import org.springframework.stereotype.Repository;
import toyproject.loobie.web.dto.ArticleSaveRequestDto;

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

    public Long saveByCategory(ArticleSaveRequestDto requestDto, ArticleCategory category){
        Article article = Article.builder()
                .news(requestDto.getNews())
                .newsLink(requestDto.getNewsLink())
                .newsContent(requestDto.getNewsContent())
                .type(category)
                .build();

        em.persist(article);
        return article.getId();
    }


    public Article find(Long id){
        return em.find(Article.class, id);
    }

}
