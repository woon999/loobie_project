package toyproject.loobie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import toyproject.loobie.service.S3Service;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service downloadS3;

    @GetMapping("/csv_download")
    public ResponseEntity<byte[]> download() throws IOException {
        return downloadS3.getObject("20210527.csv");
    }

    @GetMapping("/csv_read")
    public String read() throws IOException {
        downloadS3.readObject("20210527.csv");
        return "csv_read";
    }
}
