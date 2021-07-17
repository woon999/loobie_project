package toyproject.loobie.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
public class AdminApis {

    private LocalDateTime date = LocalDateTime.now();
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private String todayDate = date.format(dateTimeFormatter);
    private String todayDataFileName =  todayDate +".csv";
    private final String kakaoUrl = "https://kapi.kakao.com";

    /*********************************** ADMIN ********************************************/
    /**
     * 카카오톡 친구 목록 불러오기
     */
    public void getFriendList() {
    }


    /**
     * 카카오톡 나에게 메시지 보내기
     */
    public void SendMessageToMe(){

    }

    /**
     * 카카오톡 친구에게 메시지 보내기
     */
    public void SendMessageToFriend(){

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
