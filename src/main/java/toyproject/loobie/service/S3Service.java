package toyproject.loobie.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import toyproject.loobie.ArticleRepository;
import toyproject.loobie.EconomicRepository;
import toyproject.loobie.NewsRepository;
import toyproject.loobie.domain.Article;
import toyproject.loobie.domain.Economic;
import toyproject.loobie.domain.Enum.ArticleCategory;
import toyproject.loobie.domain.Enum.EconomicCategory;
import toyproject.loobie.domain.News;

import javax.transaction.Transactional;
import java.io.*;
import java.net.URLEncoder;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class S3Service {

    private final AmazonS3 amazonS3;
    private final NewsRepository newsRepository;
    private final ArticleRepository articleRepository;
    private final EconomicRepository economicRepository;


    private static String content;
    private static String link;

    @Value("${aws.s3.bucket}")
    private String bucket;



    /**
     * S3 bucket 파일 읽기
     */
    public void readObject(String storedFileName) throws IOException{
        S3Object o = amazonS3.getObject(new GetObjectRequest(bucket, storedFileName));
        S3ObjectInputStream ois = null;
        BufferedReader br = null;

        // Read the CSV one line at a time and process it.
        try {
            ois = o.getObjectContent();
            System.out.println ("ois = " + ois);

            br = new BufferedReader (new InputStreamReader (ois, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            Long newsId = 0L;
            while ((line = br.readLine()) != null) {
                // Store 1 record in an array separated by commas

                String[] data = line.split("#", 0);
                News news = new News();

                if(data[0].equals("N")){
                    newsId = create(news);

                    for (String s : data) {
                        System.out.print(s);
                    }
                    System.out.println();
                }
                else{

                    int idx =Integer.parseInt(data[0]);

                    for(int i=1; i<data.length; i++){
                        data[i] = data[i].substring(1, data[i].length());
                        content = null;
                        link = null;
                        Article article = new Article();
                        Economic economic = new Economic();
                        News insertNews = newsRepository.find(newsId);

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
                            stringProcessing();

                            String[] economic_data = content.split("@");
                            String index = economic_data[0].replaceAll(",","");

                            String[] changeData = economic_data[1].split("\\(");;
                            String changeIndex = changeData[0].substring(0,changeData[0].length());

                            changeData = changeData[1].split("%");
                            String changeRate = changeData[0].substring(0,changeData[0].length());

                            economic = Economic.createEconomicData(insertNews, EconomicCategory.EXCHANGE, index,changeIndex,changeRate );
                            economicRepository.save(economic);
                        }else if(data[i].equals("[경제 나스닥]")){
                            content = data[i + 1].substring(1, data[i + 1].length());
                            stringProcessing();
                            String[] economic_data = content.split("@");
                            String index = economic_data[0].replaceAll(",","");

                            String[] changeData = economic_data[1].split("\\(");;
                            String changeIndex = changeData[0].substring(0,changeData[0].length());

                            changeData = changeData[1].split("%");
                            String changeRate = changeData[0].substring(0,changeData[0].length());

                            economic = Economic.createEconomicData(insertNews, EconomicCategory.NASDAQ, index,changeIndex,changeRate );
                            economicRepository.save(economic);
                        }else if(data[i].equals("[경제 SP]")){
                            content = data[i + 1].substring(1, data[i + 1].length());
                            stringProcessing();
                            String[] economic_data = content.split("@");
                            String index = economic_data[0].replaceAll(",","");

                            String[] changeData = economic_data[1].split("\\(");;
                            String changeIndex = changeData[0].substring(0,changeData[0].length());

                            changeData = changeData[1].split("%");
                            String changeRate = changeData[0].substring(0,changeData[0].length());

                            economic = Economic.createEconomicData(insertNews, EconomicCategory.SP500, index,changeIndex,changeRate );
                            economicRepository.save(economic);
                        }
                    }

                    System.out.println();
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

    static void stringProcessing(){
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

    /**
     * S3 bucket 파일 다운로드
     * bucket 이름 : ${aws.s3.bucket}
     * bucket에 저장된 파일명: storedFileName
     */
    public ResponseEntity<byte[]> getObject(String storedFileName) throws IOException{
        S3Object o = amazonS3.getObject(new GetObjectRequest(bucket, storedFileName));
        S3ObjectInputStream objectInputStream = o.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        String fileName = URLEncoder.encode(storedFileName, "UTF-8").replaceAll("\\+", "%20");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setContentLength(bytes.length);
        httpHeaders.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }

    public Long create(News news){
        newsRepository.save(news);
        return news.getId();
    }


}
