package toyproject.loobie.domain.article;

import lombok.*;
import toyproject.loobie.domain.BaseTimeEntity;
import toyproject.loobie.domain.news.News;

import javax.persistence.*;

@Entity
@Table(name = "articles")
@Getter @Setter
@NoArgsConstructor
public class Article extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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


    @Builder
    public Article(ArticleCategory type, String newsContent, String newsLink, News news) {
        this.type = type;
        this.newsContent = newsContent;
        this.newsLink = newsLink;
        this.news = news;
    }

}
