package toyproject.loobie.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import toyproject.loobie.config.auth.LoginUser;
import toyproject.loobie.config.auth.dto.SessionUser;
import toyproject.loobie.domain.news.News;
import toyproject.loobie.domain.user.User;
import toyproject.loobie.service.NewsService;
import toyproject.loobie.service.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller @Slf4j
@RequiredArgsConstructor
public class AdminController {

    private final NewsService newsService;
    private final UserService userService;
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
            attributes.addFlashAttribute("readMessage", "이미 뉴스를 받아왔습니다.");
            return "redirect:/admin";
        }

        // TODO : S3 파일 없을 경우 예외처리
        newsService.readAndSaveBucketObject(todayDataFileName);
        attributes.addFlashAttribute("readMessage", "뉴스 받아오기에 성공하였습니다.");

        return "redirect:/admin";
    }

    /**
     * admin S3에서 date로 조회하여 뉴스 받아오기
     */
    @Scheduled(cron = "0 55 0 * * *", zone="Asia/Seoul")
    @PostMapping("/admin/s3read")
    public String s3ReadNewsByDate(@RequestParam("newsDate") String date, RedirectAttributes attributes) throws IOException {
        List<News> newsList = newsService.findByDate(date);

        // 이미 뉴스를 읽어왔을 경우 그냥 리턴
        if(newsList.size() != 0){
            attributes.addFlashAttribute("dateReadMessage", "이미 뉴스를 받아왔습니다.");
            return "redirect:/admin";
        }

        // TODO : S3 파일 없을 경우 예외처리
        String fileName =  date +".csv";
        newsService.readAndSaveBucketObject(fileName);
        attributes.addFlashAttribute("dateReadMessage", "뉴스 받아오기에 성공하였습니다.");

        return "redirect:/admin";
    }

    /**
     * 구독한 유저에게 뉴스 메일 전송하기
     */
    @Scheduled(cron = "0 0 1 * * *", zone="Asia/Seoul")
    @GetMapping("/admin/send-news")
    public String sendNewsEmail(RedirectAttributes attributes){
        List<User> userList = userService.findAll();
        List<News> newsList = newsService.findByDate(todayDate);

        if(userList.size()==0){
            // 오늘의 뉴스가 없을 경우 에러 처리
            attributes.addFlashAttribute("noDataMessage", "등록된 유저가 없습니다.");
            return "redirect:/admin";
        }
        if(newsList.size()==0){
            // 오늘의 뉴스가 없을 경우 에러 처리
            attributes.addFlashAttribute("noDataMessage", "뉴스는 매일 오전 8시 업데이트됩니다.");
            return "redirect:/admin";
        }
        News news = newsList.get(0);
        for(User user : userList){
            String to = user.getEmail();
            log.info(to+ "님에게 뉴스 전송 완료");
            userService.sendNewsEmail(to, news);
        }

        attributes.addFlashAttribute("sendMessage", "뉴스 보내기 성공하였습니다.");
        return "redirect:/admin";
    }

}
