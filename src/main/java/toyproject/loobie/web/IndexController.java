package toyproject.loobie.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import toyproject.loobie.domain.news.News;
import toyproject.loobie.service.NewsService;
import toyproject.loobie.web.dto.NewsReadRequestDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final NewsService newsService;
    private LocalDateTime date = LocalDateTime.now();
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private String todayDate = date.format(dateTimeFormatter);


/*********************************** ALL ********************************************/
    /**
     * home view
     */
    @GetMapping("/")
    public String home(){
        return "client/home";
    }

    /**
     * today newsList view
     */
    @GetMapping("/news/today")
    public String readTodayNews(Model model, RedirectAttributes attributes){
        List<News> newsList = newsService.findByDate(todayDate);
        if(newsList.size()==0){
            // 오늘의 뉴스가 없을 경우 에러 처리
            attributes.addFlashAttribute("message", "뉴스는 매일 오전 8시 업데이트됩니다.");
            return "redirect:/";
        }
        News news = newsList.get(0);
        model.addAttribute("articles", news.getArticles());
        model.addAttribute("economics", news.getEconomics());
        return "news/newsList";
    }


    /**
     * newsSearch view
     */
    @GetMapping("/search")
    public String searchNews(Model model){
        model.addAttribute("searchNews", new NewsReadRequestDto());
        return "client/search";
    }

    /**
     * {date} newsList view
     */
    @GetMapping("/news/{newsDate}")
    public String readNewsByDate(@PathVariable("newsDate") String date, Model model, RedirectAttributes attributes){

        List<News> newsList = newsService.findByDate(date);
        if(newsList.size() == 0){
            attributes.addFlashAttribute("noDataMessage", "해당 날짜의 뉴스는 존재하지 않습니다.");
            return "redirect:/search";
        }
        News news = newsList.get(0);
        model.addAttribute("articles", news.getArticles());
        model.addAttribute("economics", news.getEconomics());

        return "news/newsList";
    }


}
