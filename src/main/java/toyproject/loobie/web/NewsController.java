package toyproject.loobie.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import toyproject.loobie.domain.news.News;
import toyproject.loobie.service.NewsService;
import toyproject.loobie.web.dto.NewsReadRequestDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;
    private LocalDateTime date = LocalDateTime.now();
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private String todayDate = date.format(dateTimeFormatter);
    private String todayDataFileName =  todayDate +".csv";

    @GetMapping("/api/news/read")
    public void s3ReadNews() throws IOException {
        newsService.readBucketObject(todayDataFileName);
    }

    /**
     * 날짜로 뉴스 조회
     */
    @PostMapping("/api/news/search/{date}")
    public void searchNewsByDate(){
//        newsService.findByDate()
    }

//    @GetMapping("/csv_download")
//    public ResponseEntity<byte[]> downloadS3Object(){
//        try {
//            return newsService.getBucketObject(todayDataFileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }



}
