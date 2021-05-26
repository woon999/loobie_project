package toyproject.loobie.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class News {

    @Id @GeneratedValue
    @Column(name = "news_id")
    private Long id;

    @OneToMany(mappedBy = "news")
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "news")
    private List<Economic> economics = new ArrayList<>();

}
