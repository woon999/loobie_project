package toyproject.loobie.domain.article;

import lombok.*;
import toyproject.loobie.domain.news.News;

import javax.persistence.*;

@Entity
@Table(name = "articles")
@Getter @Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id @GeneratedValue
    @Column(name = "article_id")
    private Long id;

    // ENUM 정치, 경제, IT, 해외
    @Enumerated(EnumType.STRING)
    private ArticleCategory type;

    private String newsContent;
    private String newsLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    public Article(){

    }

    //==생성 메서드==//
    public static Article createArticle(News news, ArticleCategory type, String content, String link) {
        Article article = new Article();
        article.setNews(news);
        article.setType(type);
        article.setNewsContent(content);
        article.setNewsLink(link);

        return article;
    }

}
