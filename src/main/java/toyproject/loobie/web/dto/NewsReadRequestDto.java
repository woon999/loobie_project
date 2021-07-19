package toyproject.loobie.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class NewsReadRequestDto {

    private String NewsDate;
    private String date;

    private String newsContent;
    private String newsLink;

    private String index;
    private String changeIndex;
    private String changeRate;

}
