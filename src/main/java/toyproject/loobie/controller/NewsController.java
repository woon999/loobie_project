package toyproject.loobie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import toyproject.loobie.domain.News;
import toyproject.loobie.service.NewsService;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/searchNews")
    public String searchNews(Model model){
        model.addAttribute("searchNews", new NewsForm());
        return "news/searchNews";
    }

    /**
     * 날짜로 뉴스 조회
     * @param newsForm
     * @param model
     * @param result
     * @return
     */
    @PostMapping("/searchNews")
    public String searchNewsByDate(NewsForm newsForm ,Model model, BindingResult result){
        if(result.hasErrors()){
            return "news/searchNews";
        }

        List<News> newsList = newsService.findByDate(newsForm.getDate());
        if(newsList.size() == 0){
            return "redirect:/";
        }
        News news = newsList.get(0);
//        System.out.println("news Id = " + newsId);
//        for(Article article : news.getArticles()){
//            System.out.println("-------------------------------");
//            System.out.println(article.getType());
//            System.out.println(article.getNewsContent());
//            System.out.println(article.getNewsLink());
//            System.out.println("-------------------------------");
//        }
//        for(Economic economic : news.getEconomics()){
//            System.out.println("-------------------------------");
//            System.out.println(economic.getType());
//            System.out.println(economic.getIndex() + " ," +economic.getChangeIndex() +" ," + economic.getChangeRate());
//            System.out.println("-------------------------------");
//        }

        model.addAttribute("articles", news.getArticles());
        model.addAttribute("economics", news.getEconomics());

        return "news/newsList";
    }

    @GetMapping("/news/date/{newsDate}")
    public String readNewsByDate(@PathVariable("newsDate") String date, Model model){

        List<News> newsList = newsService.findByDate(date);
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
