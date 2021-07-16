package toyproject.loobie.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminApis {

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
}
