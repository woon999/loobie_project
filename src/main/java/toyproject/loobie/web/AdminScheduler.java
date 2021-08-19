package toyproject.loobie.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
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

@Component @Slf4j
@RequiredArgsConstructor
public class AdminScheduler {

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
    @Scheduled(cron = "0 0/1 * * * *", zone="Asia/Seoul")
    public void autoS3ReadNews() throws IOException {
        List<News> newsList = newsService.findByDate(todayDate);

        // 이미 뉴스를 읽어왔을 경우 그냥 리턴
//        if(newsList.size() != 0){
//            log.error("이미 뉴스를 읽었습니다.");
//            return;
//        }
        // TODO : S3 파일 없을 경우 예외처리
        newsService.readAndSaveBucketObject(todayDataFileName);
    }


    /**
     * 구독한 유저에게 뉴스 메일 전송하기
     */
    @Scheduled(cron = "0 0/1 * * * *", zone="Asia/Seoul")
    public void autoSendNewsEmail(){
        List<User> userList = userService.findAll();
        List<News> newsList = newsService.findByDate(todayDate);

        if(userList.size()==0){
            // 유저가 없을 경우 에러처리
            return;
        }
        if(newsList.size()==0){
            // 오늘의 뉴스가 없을 경우 에러 처리
            return;
        }
        News news = newsList.get(0);
        for(User user : userList){
            String to = user.getEmail();
            log.info(to+ "님에게 뉴스 전송 완료");
            userService.sendNewsEmail(to, news);
        }
    }

}
