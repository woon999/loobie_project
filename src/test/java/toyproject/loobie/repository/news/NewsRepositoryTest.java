package toyproject.loobie.repository.news;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import toyproject.loobie.domain.article.Article;
import toyproject.loobie.domain.article.ArticleRepository;
import toyproject.loobie.domain.economic.Economic;
import toyproject.loobie.domain.economic.EconomicRepository;
import toyproject.loobie.domain.news.News;
import toyproject.loobie.domain.news.NewsRepository;
import toyproject.loobie.service.NewsService;
import toyproject.loobie.web.dto.ArticleSaveRequestDto;
import toyproject.loobie.web.dto.EconomicSaveRequestDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static toyproject.loobie.domain.article.ArticleCategory.*;
import static toyproject.loobie.domain.economic.EconomicCategory.*;

@RunWith(SpringRunner.class)
@SpringBootTest @Transactional
public class NewsRepositoryTest {
    @Autowired
    NewsRepository newsRepository;
    @Autowired
    NewsService newsService;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    EconomicRepository economicRepository;

    @After
    public void cleanup(){
        economicRepository.clear();
        articleRepository.clear();
        newsRepository.clear();
    }

    @Test
    public void 뉴스_기사_경제_저장_불러오기(){
        // given
        News news = new News();
        String date = "20210629";
        Long newsId = newsService.create(news, date);
        News insertNews = newsRepository.findOne(newsId);

        // 기사 데이터 추가
        String content = "content";
        String link = "link";
        ArticleSaveRequestDto requestDto = ArticleSaveRequestDto.builder()
                .newsContent(content)
                .newsLink(link)
                .news(insertNews)
                .build();
        articleRepository.saveByCategory(requestDto ,POLITICS);


        // 경제 데이터 추가
        String eIndex = "100";
        String changeIndex = "101";
        String changeRate = "102";
        EconomicSaveRequestDto requestDto2 = EconomicSaveRequestDto.builder()
                .eIndex(eIndex)
                .changeIndex(changeIndex)
                .changeRate(changeRate)
                .news(insertNews)
                .build();
        economicRepository.saveByCategory(requestDto2, EXCHANGE);

        News resultNews = newsRepository.findByDate(date).get(0);
        assertThat(resultNews.getDate()).isEqualTo(date);

        Article article = articleRepository.find(1L);
        assertThat(article.getNewsContent()).isEqualTo("content");
        assertThat(article.getNewsLink()).isEqualTo("link");

        Economic economic = economicRepository.find(1L);
        assertThat(economic.getEIndex()).isEqualTo("100");
        assertThat(economic.getChangeIndex()).isEqualTo("101");
        assertThat(economic.getChangeRate()).isEqualTo("102");
    }

    @Test
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