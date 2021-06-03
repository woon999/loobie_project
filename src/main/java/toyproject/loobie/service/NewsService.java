package toyproject.loobie.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.loobie.repository.NewsRepository;
import toyproject.loobie.domain.News;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public News findOne(Long id) {
        return newsRepository.findOne(id);
    }

    public List<News> findByDate(String date) {
        return newsRepository.findDate(date);
    }

    public List<News> findAllNews(){
        return newsRepository.findAll();
    }
}
