package toyproject.loobie.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import toyproject.loobie.domain.article.ArticleCategory;
import toyproject.loobie.domain.article.ArticleRepository;
import toyproject.loobie.domain.economic.EconomicCategory;
import toyproject.loobie.domain.economic.EconomicRepository;
import toyproject.loobie.domain.news.NewsRepository;
import toyproject.loobie.domain.news.News;
import toyproject.loobie.web.dto.ArticleSaveRequestDto;
import toyproject.loobie.web.dto.EconomicSaveRequestDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static toyproject.loobie.domain.article.ArticleCategory.*;
import static toyproject.loobie.domain.economic.EconomicCategory.*;

@Service @Slf4j
@RequiredArgsConstructor
public class NewsService {

    private final AmazonS3 amazonS3;
    private final NewsRepository newsRepository;
    private final ArticleRepository articleRepository;
    private final EconomicRepository economicRepository;

    private static String content;
    private static String link;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Transactional(readOnly = true)
    public News findOne(Long id) {
        return newsRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<News> findByDate(String date) {
        return newsRepository.findByDate(date);
    }
    @Transactional(readOnly = true)
    public List<News> findAll(){
        return newsRepository.findAll();
    }

    /**
     * news DB에 저장 + Date 정보
     */
    @Transactional
    public Long create(News news, String date){
        newsRepository.saveWithDate(news, date);
        return news.getId();
    }


    /**
     * S3 bucket 객체 파일 읽어서 저장하기 (csv)
     */
    @Transactional
    public boolean readAndSaveBucketObject(String storedFileName) throws IOException {
        S3Object o = amazonS3.getObject(new GetObjectRequest(bucket, storedFileName));

        boolean flag = false;
        // CSV 한 번에 한 줄씩 읽기
        try (S3ObjectInputStream ois = o.getObjectContent();
        BufferedReader br = new BufferedReader (new InputStreamReader(ois, "UTF-8"))){

            String line = null;
            String date;
            Long newsId = 0L;
            while ((line = br.readLine()) != null) {
                flag= true;
                // Store 1 record in an array separated by commas
                String[] data = line.split("#", 0);
                News news = new News();

                if(data[0].equals("N")){
                    date = storedFileName.substring(0,8);
                    newsId = create(news, date);
                    continue;
                }

                for(int i=1; i<data.length; i++){
                    data[i] = data[i].substring(1, data[i].length());
                    content = null;
                    link = null;
                    News insertNews = newsRepository.findOne(newsId);

                    if(data[i].equals("[뉴스 정치]")){
                        trimContentAndLink(data, i);
                        trimCsvData();
                        saveArticleByType(insertNews, POLITICS);
                    }else if(data[i].equals("[뉴스 경제]")){
                        trimContentAndLink(data, i);
                        trimCsvData();
                        saveArticleByType(insertNews, ECONOMIC);
                    }else if(data[i].equals("[뉴스 IT/과학]")){
                        trimContentAndLink(data, i);
                        trimCsvData();
                        saveArticleByType(insertNews, IT);
                    }else if(data[i].equals("[뉴스 CNBC]")){
                        trimContentAndLink(data, i);
                        trimCsvData();
                        saveArticleByType(insertNews, CNBC);
                    }else if(data[i].equals("[경제 환율]")){
                        saveEconomicDataByType(insertNews, data[i + 1], EXCHANGE);
                    }else if(data[i].equals("[경제 나스닥]")){
                        saveEconomicDataByType(insertNews, data[i + 1], NASDAQ);
                    }else if(data[i].equals("[경제 SP]")){
                        saveEconomicDataByType(insertNews, data[i + 1], SP500);
                    }
                }
            }
        }

        return flag;
    }

    private void saveEconomicDataByType(News insertNews, String datum, EconomicCategory economicType) {
        content = datum.substring(1, datum.length());
        String[] pData = trimEconomicData().split(",");

        EconomicSaveRequestDto requestDto = EconomicSaveRequestDto.builder()
            .eIndex(pData[0])
            .changeIndex(pData[1])
            .changeRate(pData[2])
            .news(insertNews)
            .build();
        economicRepository.saveByCategory(requestDto, economicType);
    }

    private void trimContentAndLink(String[] data, int i) {
        content = data[i + 1].substring(1, data[i + 1].length());
        link = data[i + 2].substring(1, data[i + 2].length());
    }

    private void saveArticleByType(News insertNews, ArticleCategory articleType) {
        ArticleSaveRequestDto requestDto = ArticleSaveRequestDto.builder()
            .newsContent(content)
            .newsLink(link)
            .news(insertNews)
            .build();
        articleRepository.saveByCategory(requestDto, articleType);
    }

    // csv 데이터 1차 가공 작업
    private void trimCsvData(){
        if(content != null){
            if(content.charAt(0) ==',') {
                content = content.substring(1, content.length());
            }
            if(content.charAt(0) =='"') {
                content = content.substring(1, content.length());
            }
            if(content.charAt(content.length()-1) =='"') {
                content = content.substring(0, content.length()-1);
            }
            content = content.replaceAll("\"\"", "\"");
            System.out.println(content);
        }
        if(link != null ){
            if(link.charAt(0) ==',') {
                link = link.substring(1, link.length());
            }
            System.out.println(link);
        }
    }

    // 경제 데이터 String 2차 가공 작업
    private String trimEconomicData(){
        trimCsvData();
        String[] economic_data = content.split("@");
        String index = economic_data[0].replaceAll(",","");

        String[] changeData = economic_data[1].split("\\(");;
        String changeIndex = changeData[0].substring(0,changeData[0].length());

        changeData = changeData[1].split("%");
        String changeRate = changeData[0].substring(0,changeData[0].length());

        return index+","+changeIndex+","+changeRate;
    }

}
