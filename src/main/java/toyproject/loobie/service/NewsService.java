package toyproject.loobie.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.loobie.NewsRepository;
import toyproject.loobie.domain.News;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public News findOne(Long id) {
        return newsRepository.findOne(id);
    }
}
