package toyproject.loobie.controller;

import lombok.RequiredArgsConstructor;
import org.dom4j.rule.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import toyproject.loobie.domain.Article;
import toyproject.loobie.domain.Economic;
import toyproject.loobie.domain.News;
import toyproject.loobie.service.NewsService;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/news/date")
    public String searchNews(Model model){
        model.addAttribute("searchNews", new NewsForm());
        return "news/searchNews";
    }

    // TODO : 날짜검색 후 조회
    @PostMapping("/news/date")
    public String readNews(NewsForm newsForm , BindingResult result){
        if(result.hasErrors()){
            return "news/searchNews";
        }

        List<News> newsList = newsService.findDate(newsForm.getDate());
        if(newsList == null){
            return "redirect:/";
        }
        News news = newsList.get(0);

        Long newsId = news.getId();
        System.out.println("news Id = " + newsId);
        for(Article article : news.getArticles()){
            System.out.println("-------------------------------");
            System.out.println(article.getType());
            System.out.println(article.getNewsContent());
            System.out.println(article.getNewsLink());
            System.out.println("-------------------------------");
        }
        for(Economic economic : news.getEconomics()){
            System.out.println("-------------------------------");
            System.out.println(economic.getType());
            System.out.println(economic.getIndex() + " ," +economic.getChangeIndex() +" ," + economic.getChangeRate());
            System.out.println("-------------------------------");
        }

        return "news/" + newsId;
    }

    @GetMapping("/news/date/{newsDate}")
    public String readNewsWithDate(@PathVariable("newsDate") String date, Model model){

        List<News> newsList = newsService.findDate(date);
        if(newsList == null){
            return "redirect:/";
        }
        News news = newsList.get(0);
        model.addAttribute("articles", news.getArticles());
        model.addAttribute("economics", news.getEconomics());

        return "news/newsList";

    }

    @GetMapping("/news/{newsId}")
    public String readNews(@PathVariable("newsId") Long newsId, Model model){

        News news = newsService.findOne(newsId);
        if(news == null){
            return "redirect:/";
        }else{
            model.addAttribute("articles", news.getArticles());
            model.addAttribute("economics", news.getEconomics());

            return "news/newsList";
        }


    }

}
