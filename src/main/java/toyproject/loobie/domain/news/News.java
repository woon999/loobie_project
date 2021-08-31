package toyproject.loobie.domain.news;

import lombok.*;
import toyproject.loobie.domain.BaseTimeEntity;
import toyproject.loobie.domain.article.Article;
import toyproject.loobie.domain.economic.Economic;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class News extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long id;

    private String date;

    @OneToMany(mappedBy = "news")
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "news")
    private List<Economic> economics = new ArrayList<>();

    @Builder
    public News(String date, List<Article> articles, List<Economic> economics) {
        this.date = date;
        this.articles = articles;
        this.economics = economics;
    }
}
