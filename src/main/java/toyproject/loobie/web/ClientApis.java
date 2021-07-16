package toyproject.loobie.web;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import toyproject.loobie.service.UserService;
import toyproject.loobie.web.dto.UserSaveRequestDto;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ClientApis {

    private final UserService userService;

    /*********************************** CLIENT ********************************************/
    /**
     * 구독하기
     */
    @PostMapping("/api/v1/user")
    public Map<String,Long> clientSave(@RequestBody UserSaveRequestDto requestDto){
        if(userService.findByEmail(requestDto.getEmail())){
            // TODO : 이미 구독한 유저 에러 처리
        }
        Long id = userService.save(requestDto);
        System.out.println("###user 생성 " + id);
        return Map.of("success",id);
    }

    /**
     * 날짜로 뉴스 조회
     */
    @GetMapping("/api/news/search/{date}")
    public void searchNewsByDate(){
//        newsService.findByDate()
    }
}
