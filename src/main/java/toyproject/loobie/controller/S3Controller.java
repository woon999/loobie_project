package toyproject.loobie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import toyproject.loobie.service.S3Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;
    private LocalDateTime date = LocalDateTime.now();
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private String todayDate = date.format(dateTimeFormatter);
    private String todayDataFileName =  todayDate +".csv";

    @GetMapping("/csv_download")
    public ResponseEntity<byte[]> download(){
        try {
            return s3Service.getObject(todayDataFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/csv_read")
    public String read(){
        try {
            s3Service.readObject(todayDataFileName);
            return "csv_read";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }
}
