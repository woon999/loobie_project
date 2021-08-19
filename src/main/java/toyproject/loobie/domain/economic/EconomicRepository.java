package toyproject.loobie.domain.economic;

import org.springframework.stereotype.Repository;
import toyproject.loobie.web.dto.EconomicSaveRequestDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class EconomicRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Economic economic){
        em.persist(economic);
        return economic.getId();
    }

    public Long saveByCategory(EconomicSaveRequestDto requestDto, EconomicCategory category){
        Economic economic = Economic.builder()
                .news(requestDto.getNews())
                .eIndex(requestDto.getEIndex())
                .changeIndex(requestDto.getChangeIndex())
                .changeRate(requestDto.getChangeRate())
                .type(category)
                .build();

        em.persist(economic);
        return economic.getId();
    }

    public Economic find(Long id){
        return em.find(Economic.class, id);
    }

}
