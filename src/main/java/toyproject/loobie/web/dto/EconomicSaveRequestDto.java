package toyproject.loobie.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.loobie.domain.article.Article;
import toyproject.loobie.domain.economic.Economic;
import toyproject.loobie.domain.economic.EconomicCategory;
import toyproject.loobie.domain.news.News;

import javax.persistence.*;

@Getter
@NoArgsConstructor
public class EconomicSaveRequestDto {

    private String eIndex;
    private String changeIndex;
    private String changeRate;
    private News news;

    @Builder
    public EconomicSaveRequestDto(String eIndex, String changeIndex, String changeRate, News news) {
        this.eIndex = eIndex;
        this.changeIndex = changeIndex;
        this.changeRate = changeRate;
        this.news = news;
    }

    public Economic toEntity(){
        return Economic.builder()
                .eIndex(eIndex)
                .changeIndex(changeIndex)
                .changeRate(changeRate)
                .news(news)
                .build();
    }
}
