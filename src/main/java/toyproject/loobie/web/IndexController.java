package toyproject.loobie.web;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import toyproject.loobie.config.auth.LoginUser;
import toyproject.loobie.config.auth.dto.SessionUser;
import toyproject.loobie.domain.news.News;
import toyproject.loobie.service.NewsService;
import toyproject.loobie.service.UserService;
import toyproject.loobie.web.dto.NewsReadRequestDto;
import toyproject.loobie.web.dto.UserSaveRequestDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final NewsService newsService;
    private final UserService userService;
    private LocalDateTime date = LocalDateTime.now();
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private String todayDate = date.format(dateTimeFormatter);
    private String todayDataFileName =  todayDate +".csv";

    /*********************************** CLIENT ********************************************/
    /**
     * client home view
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
     * TODO
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


    /*********************************** ADMIN ********************************************/
    /**
     * admin home view
     */
    @GetMapping("/admin")
    public String adminHome(Model model, @LoginUser SessionUser user){
        if(user != null){
            model.addAttribute("userName", user.getName());
        }
        return "admin/home";
    }

    /**
     * admin S3에서 뉴스 받아오기
     * TODO : 스케줄링 자동 설정
     */
    @GetMapping("/admin/s3read")
    public String s3ReadNews(RedirectAttributes attributes) throws IOException {
        List<News> newsList = newsService.findByDate(todayDate);

        // 이미 뉴스를 읽어왔을 경우 그냥 리턴
        if(newsList.size() != 0){
            attributes.addFlashAttribute("message", "이미 뉴스를 받아왔습니다.");
            return "redirect:/admin";
        }

        newsService.readBucketObject(todayDataFileName);
        attributes.addFlashAttribute("message", "뉴스 받아오기에 성공하였습니다.");

        return "redirect:/admin";
    }
}
