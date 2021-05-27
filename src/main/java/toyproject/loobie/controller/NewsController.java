package toyproject.loobie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import toyproject.loobie.NewsRepository;
import toyproject.loobie.domain.News;
import toyproject.loobie.service.NewsService;


@Controller
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/news")
    public String searchNews(Model model){
        model.addAttribute("searchNews", new NewsForm());
        return "news/searchNews";
    }


    @GetMapping("/news/{newsId}")
    public void readNews(@PathVariable("newsId") Long newsId, Model model){

        News news = newsService.findOne(newsId);
        NewsForm form = new NewsForm();

//        form.setId(news.getId());
//        form.setNewsDate(form.setNewsDate());
//        form.setNewsContent();

    }

}
