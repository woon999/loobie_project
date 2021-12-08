package toyproject.loobie.service.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Target;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * admin S3에서 뉴스 받아오기
     * 매일 오전 6시 55분
     */
    @Transactional
    @Scheduled(cron = "0 0/30 * * * *", zone="Asia/Seoul")
//    @Scheduled(cron = "0 55 6 * * *", zone="Asia/Seoul")
    public void autoS3ReadNews() throws IOException {
        List<News> newsList = newsService.findByDate(todayDate);

        // 이미 뉴스를 읽어왔을 경우 그냥 리턴
        if(newsList.size() != 0){
            log.error("이미 뉴스를 읽었습니다.");
            return;
        }
        log.info("뉴스 읽기 시작");
        boolean status = newsService.readAndSaveBucketObject(todayDataFileName);
        if(status){
            log.info("뉴스 읽기 성공");
        }else{
            log.info("뉴스 읽기 실패");
        }

    }


    /**
     * 구독한 유저에게 뉴스 메일 전송하기
     * 매일 오전 6시 59분
     */
    @Transactional(readOnly = true)
    @Scheduled(cron = "0 0/30 * * * *", zone="Asia/Seoul")
//    @Scheduled(cron = "0 59 6 * * *", zone="Asia/Seoul")
    public void autoSendNewsEmail(){
        List<User> userList = userService.findAll();
        List<News> newsList = newsService.findByDate(todayDate);

        if(userList.size()==0){
            // 유저가 없을 경우 에러처리
            log.error("뉴스를 전송할 유저가 존재하지 않습니다.");
            return;
        }
        if(newsList.size()==0){
            // 오늘의 뉴스가 없을 경우 에러 처리
            log.error("오늘 뉴스가 존재하지 않습니다.");
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
