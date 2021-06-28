package toyproject.loobie.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import toyproject.loobie.domain.news.News;
import toyproject.loobie.service.NewsService;
import toyproject.loobie.web.dto.NewsReadRequestDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;
    private LocalDateTime date = LocalDateTime.now();
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private String todayDate = date.format(dateTimeFormatter);
    private String todayDataFileName =  todayDate +".csv";


    @GetMapping("/searchNews")
    public String searchNews(Model model){
        model.addAttribute("searchNews", new NewsReadRequestDto());
        return "news/searchNews";
    }

    /**
     * 날짜로 뉴스 조회
     */
    @PostMapping("/searchNews")
    public String searchNewsByDate(NewsReadRequestDto newsForm , Model model, BindingResult result){
        if(result.hasErrors()){
            return "news/searchNews";
        }
        List<News> newsList = newsService.findByDate(newsForm.getDate());

        // news가 없을 경우
        if(newsList.size() == 0){
            return "redirect:/";
        }
        News news = newsList.get(0);

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

//    @GetMapping("/csv_download")
//    public ResponseEntity<byte[]> downloadS3Object(){
//        try {
//            return newsService.getBucketObject(todayDataFileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    @GetMapping("/csv_read")
    public String s3ReadNews(){
        try {
            newsService.readBucketObject(todayDataFileName);
            return "csv_read";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

}
