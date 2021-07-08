package toyproject.loobie.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import toyproject.loobie.config.auth.LoginUser;
import toyproject.loobie.config.auth.dto.SessionUser;
import toyproject.loobie.domain.news.News;
import toyproject.loobie.service.NewsService;
import toyproject.loobie.web.dto.NewsReadRequestDto;

import java.io.IOException;
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
    private String todayDataFileName =  todayDate +".csv";

    /**
     * home view
     */
    @GetMapping("/")
    public String home(Model model, @LoginUser SessionUser user){
        if(user != null){
            model.addAttribute("userName", user.getName());
        }
        return "home";
    }

    /**
     * today newsList view
     */
    @GetMapping("/news/today")
    public String readTodayNews(Model model){

        List<News> newsList = newsService.findByDate(todayDate);
        if(newsList.size()==0){
            // 오늘의 뉴스가 없을 경우 에러 처리
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
    @GetMapping("/news/search")
    public String searchNews(Model model, @LoginUser SessionUser user){
        model.addAttribute("searchNews", new NewsReadRequestDto());
        if(user != null){
            model.addAttribute("userName", user.getName());
        }
        return "news/search";
    }


    /**
     * {date} newsList view
     */
    @GetMapping("/news/{newsDate}")
    public String readNewsByDate(@PathVariable("newsDate") String date, Model model){

        List<News> newsList = newsService.findByDate(date);
        if(newsList.size() == 0){
            // 오늘의 뉴스가 없을 경우 에러 처리
            return "redirect:/";
        }
        News news = newsList.get(0);
        model.addAttribute("articles", news.getArticles());
        model.addAttribute("economics", news.getEconomics());

        return "news/newsList";
    }

    /**
     * Role: ADMIN
     * S3에서 뉴스 받아오기
     * TODO : 스케줄링 자동 설정
     */
    @GetMapping("/news/s3read")
    public String s3ReadNews(RedirectAttributes attributes) throws IOException {
        List<News> newsList = newsService.findByDate(todayDate);

        // 이미 뉴스를 읽어왔을 경우 그냥 리턴
        if(newsList.size() != 0){
            attributes.addFlashAttribute("message", "이미 뉴스를 받아왔습니다.");
            return "redirect:/";
        }

        newsService.readBucketObject(todayDataFileName);
        attributes.addFlashAttribute("message", "뉴스 받아오기에 성공하였습니다.");

        return "redirect:/";
    }
}
