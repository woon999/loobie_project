package toyproject.loobie.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import toyproject.loobie.config.auth.LoginUser;
import toyproject.loobie.config.auth.dto.SessionUser;
import toyproject.loobie.domain.news.News;
import toyproject.loobie.service.NewsService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final NewsService newsService;
    private LocalDateTime date = LocalDateTime.now();
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private String todayDate = date.format(dateTimeFormatter);
    private String todayDataFileName =  todayDate +".csv";

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
     *TODO : 스케줄링 자동 설정
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
