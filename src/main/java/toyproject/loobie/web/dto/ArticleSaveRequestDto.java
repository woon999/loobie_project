package toyproject.loobie.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.loobie.domain.article.Article;
import toyproject.loobie.domain.news.News;


@Getter
@NoArgsConstructor
public class ArticleSaveRequestDto {

    private String newsContent;
    private String newsLink;
    private News news;


    @Builder
    public ArticleSaveRequestDto(String newsContent, String newsLink, News news) {
        this.newsContent = newsContent;
        this.newsLink = newsLink;
        this.news = news;
    }

    public Article toEntity(){
        return Article.builder()
                .newsContent(newsContent)
                .newsLink(newsLink)
                .news(news)
                .build();
    }
}
