package toyproject.loobie.domain.economic;

import lombok.*;
import toyproject.loobie.domain.BaseTimeEntity;
import toyproject.loobie.domain.news.News;

import javax.persistence.*;

@Entity
@Table(name = "economics")
@Getter @Setter
public class Economic extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "economic_id")
    private Long id;

    // ENUM 환율, 나스닥, SP500
    @Enumerated(EnumType.STRING)
    private EconomicCategory type;

    private String eIndex;
    private String changeIndex;
    private String changeRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    public Economic(){
    }

    @Builder
    public Economic(EconomicCategory type, String eIndex, String changeIndex, String changeRate, News news) {
        this.type = type;
        this.eIndex = eIndex;
        this.changeIndex = changeIndex;
        this.changeRate = changeRate;
        this.news = news;
    }

}
