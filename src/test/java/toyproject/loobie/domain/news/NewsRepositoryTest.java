package toyproject.loobie.domain.news;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import toyproject.loobie.domain.article.ArticleRepository;
import toyproject.loobie.domain.economic.EconomicRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsRepositoryTest {
    @Autowired
    NewsRepository newsRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    EconomicRepository economicRepository;

    @After
    public void cleanup(){
    }

    @Test
    @Transactional
    public void 뉴스날짜로_저장_불러오기(){
        //given
        String date = "20210629";

        News news = new News();
        newsRepository.saveWithDate(news, date);

        //when
        List<News> newsList = newsRepository.findByDate(date);

        //then
        News test = newsList.get(0);
        assertThat(test.getDate()).isEqualTo(date);
    }

}