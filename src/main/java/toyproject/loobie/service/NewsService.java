package toyproject.loobie.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.loobie.domain.article.Article;
import toyproject.loobie.domain.article.ArticleCategory;
import toyproject.loobie.domain.article.ArticleRepository;
import toyproject.loobie.domain.economic.Economic;
import toyproject.loobie.domain.economic.EconomicCategory;
import toyproject.loobie.domain.economic.EconomicRepository;
import toyproject.loobie.domain.news.NewsRepository;
import toyproject.loobie.domain.news.News;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service

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
    private LocalDateTime date = LocalDateTime.now();
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private String todayDate = date.format(dateTimeFormatter);

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
     * S3 bucket 파일 다운로드
     * bucket 이름 : ${aws.s3.bucket}
     * bucket에 저장된 파일명: storedFileName
     */
//    public ResponseEntity<byte[]> getBucketObject(String storedFileName) throws IOException{
//        S3Object o = amazonS3.getObject(new GetObjectRequest(bucket, storedFileName));
//        S3ObjectInputStream objectInputStream = o.getObjectContent();
//        byte[] bytes = IOUtils.toByteArray(objectInputStream);
//
//        String fileName = URLEncoder.encode(storedFileName, "UTF-8").replaceAll("\\+", "%20");
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        httpHeaders.setContentLength(bytes.length);
//        httpHeaders.setContentDispositionFormData("attachment", fileName);
//
//        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
//    }

    /**
     * S3 bucket 객체 파일 읽기 (csv)
     */
    @Transactional
    public void readBucketObject(String storedFileName) throws IOException {
        S3Object o = amazonS3.getObject(new GetObjectRequest(bucket, storedFileName));
        S3ObjectInputStream ois = null;
        BufferedReader br = null;

        // CSV 한 번에 한 줄씩 읽기
        try {
            ois = o.getObjectContent();
//            System.out.println ("ois = " + ois);

            br = new BufferedReader (new InputStreamReader(ois, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            Long newsId = 0L;
            while ((line = br.readLine()) != null) {
                // Store 1 record in an array separated by commas
                String[] data = line.split("#", 0);
                News news = new News();

                if(data[0].equals("N")){
                    newsId = create(news, todayDate);
                }
                else{
//                    int idx =Integer.parseInt(data[0]);
                    for(int i=1; i<data.length; i++){
                        data[i] = data[i].substring(1, data[i].length());
                        content = null;
                        link = null;
                        Article article = new Article();
                        Economic economic = new Economic();
                        News insertNews = newsRepository.findOne(newsId);

                        // TODO : refactoring
                        if(data[i].equals("[뉴스 정치]")){
                            content = data[i + 1].substring(1, data[i + 1].length());
                            link = data[i + 2].substring(1, data[i + 2].length());
                            stringProcessing();
                            // news (POLITICS)
                            article = Article.createArticle(insertNews, ArticleCategory.POLITICS, content, link);
                            articleRepository.save(article);
                        }else if(data[i].equals("[뉴스 경제]")){
                            content = data[i + 1].substring(1, data[i + 1].length());
                            link = data[i + 2].substring(1, data[i + 2].length());
                            stringProcessing();
                            // news (ECONOMIC)
                            article = Article.createArticle(insertNews, ArticleCategory.ECONOMIC, content, link);
                            articleRepository.save(article);
                        }else if(data[i].equals("[뉴스 IT/과학]")){
                            content = data[i + 1].substring(1, data[i + 1].length());
                            link = data[i + 2].substring(1, data[i + 2].length());
                            stringProcessing();
                            // news (IT)
                            article = Article.createArticle(insertNews, ArticleCategory.IT, content, link);
                            articleRepository.save(article);
                        }else if(data[i].equals("[뉴스 CNBC]")){
                            content = data[i + 1].substring(1, data[i + 1].length());
                            link = data[i + 2].substring(1, data[i + 2].length());
                            stringProcessing();
                            // news (CNBC)
                            article = Article.createArticle(insertNews, ArticleCategory.CNBC, content, link);
                            articleRepository.save(article);
                        }else if(data[i].equals("[경제 환율]")){
                            content = data[i + 1].substring(1, data[i + 1].length());
                            String[] pData = economicDataProcessing().split(",");

                            economic = Economic.createEconomicData(insertNews, EconomicCategory.EXCHANGE,pData[0],pData[1],pData[2] );
                            economicRepository.save(economic);
                        }else if(data[i].equals("[경제 나스닥]")){
                            content = data[i + 1].substring(1, data[i + 1].length());
                            String[] pData = economicDataProcessing().split(",");

                            economic = Economic.createEconomicData(insertNews, EconomicCategory.NASDAQ, pData[0],pData[1],pData[2] );
                            economicRepository.save(economic);
                        }else if(data[i].equals("[경제 SP]")){
                            content = data[i + 1].substring(1, data[i + 1].length());
                            String[] pData = economicDataProcessing().split(",");

                            economic = Economic.createEconomicData(insertNews, EconomicCategory.SP500, pData[0],pData[1],pData[2] );
                            economicRepository.save(economic);
                        }
                    }
                }
            }
            System.out.println(sb.toString());
        }finally {
            if(ois != null){
                ois.close();
            }
            if(br != null){
                br.close();
            }
        }
    }

    // csv 데이터 1차 가공 작업
    private static void stringProcessing(){
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
    private static String economicDataProcessing(){
        stringProcessing();
        String[] economic_data = content.split("@");
        String index = economic_data[0].replaceAll(",","");

        String[] changeData = economic_data[1].split("\\(");;
        String changeIndex = changeData[0].substring(0,changeData[0].length());

        changeData = changeData[1].split("%");
        String changeRate = changeData[0].substring(0,changeData[0].length());

        return index+","+changeIndex+","+changeRate;
    }
}
