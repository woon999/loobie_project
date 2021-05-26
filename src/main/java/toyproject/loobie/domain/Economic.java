package toyproject.loobie.domain;

import lombok.*;
import toyproject.loobie.domain.Enum.ArticleCategory;
import toyproject.loobie.domain.Enum.EconomicCategory;

import javax.persistence.*;

@Entity
@Table(name = "economics")
@Getter @Setter
public class Economic {

    @Id @GeneratedValue
    private Long id;

    // ENUM 환율, 나스닥, SP500
    @Enumerated(EnumType.STRING)
    private EconomicCategory type;

    private String index;
    private String changeIndex;
    private String changeRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    public Economic(){

    }


    //==생성 메서드==//
    public static Economic createEconomicData(News news, EconomicCategory type, String
            index, String changeIndex, String changeRate) {
        Economic economic = new Economic();
        economic.setNews(news);
        economic.setType(type);
        economic.setIndex(index);
        economic.setChangeIndex(changeIndex);
        economic.setChangeRate(changeRate);

        return economic;
    }
}
