package toyproject.loobie.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RestController
@RequiredArgsConstructor
public class NewsController {

    private LocalDateTime date = LocalDateTime.now();
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private String todayDate = date.format(dateTimeFormatter);
    private String todayDataFileName =  todayDate +".csv";

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
