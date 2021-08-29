# 📬 루비 Loobie
- 프로젝트 진행 기간 21.07 ~ 알파 진행중
 
<br>

# Intro 
- 새로운 소식을 매일 전해주는 [콘텐츠 구독 서비스](https://blog.stibee.com/%EC%BD%98%ED%85%90%EC%B8%A0-%EA%B5%AC%EB%8F%85-%EC%8B%9C%EB%8C%80%EC%9D%98-%EB%89%B4%EC%8A%A4%EB%A0%88%ED%84%B0-134addfefba2)
- 기능
    - 웹 스크래핑(web scraping)으로 각 사이트의 트렌드 뉴스를 한 곳에 모아준다.
    - 해당 웹을 통해서 당일 트렌드 뉴스 혹은 지난 뉴스들을 검색해 볼 수 있다.
    - 이메일 구독을 할 경우, 매일 오전 7시 당일 트렌드 뉴스를 받아 볼 수 있다. 
  
 - 자동 스케줄링
    - python : linux crontab (뉴스 웹 스크래핑)
    - spring : @Scheduler cron (뉴스 이메일 발송)

# Stack
- Backend : Java, SpringBoot 2.4.5, JUnit4
- Frontend : bootstrap(html+css), thymeleaf
- Web Scraping : python 3.8.3
- DevOps
    - AWS EC2(Linux AMI2), RDS(MariaDB), S3
    - Github

<br>

# Project Structure
<img width="850" alt="loobie_project구조" src="https://user-images.githubusercontent.com/54282927/131255662-cf79dc24-b41f-4ec8-a904-b87e0c4bfc7f.png">

<br>

# Example
- 웹으로 뉴스 읽기 / 구독하기 
<img width="850" alt="스크린샷 2021-08-30 오전 12 40 59" src="https://user-images.githubusercontent.com/54282927/131256440-8759d464-8575-4e2b-b1e1-8861ebd46a72.png">

- 이메일로 뉴스 발송
<img width="850" alt="스크린샷 2021-08-30 오전 12 44 41" src="https://user-images.githubusercontent.com/54282927/131256574-d9547d57-ac1b-4e8c-8de2-75cb5a963469.png">


<br>

# Blog
- [[Spring/ AWS] S3에 있는 파일(CSV) 다운로드없이 바로 읽기](https://loosie.tistory.com/220?category=932704)
- [[Spring Data JPA] 메소드 네임쿼리 직접 구현 및 에러 해결(javax.persistence.NoResultException: No entity found for query)](https://loosie.tistory.com/323?category=932707)
- [[Database] MySQL과 MariaDB 무엇이 더 좋을까?](https://loosie.tistory.com/366?category=932707)
- [[Spring] 스프링 프로젝트 AWS로 배포 자동화하기 (Travis CI, CodeDeploy, S3)](https://loosie.tistory.com/403?category=932704)

<br>

# 참고한 강의 및 서적
- [자바 ORM 표준 JPA 프로그래밍 - 기본편](https://www.inflearn.com/course/ORM-JPA-Basic/dashboard)
- [실전! 스프링 부트와 JPA 활용1 - 웹 애플리케이션 개발](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-%ED%99%9C%EC%9A%A9-1/dashboard)
- [스프링과 JPA 기반 웹 애플리케이션 개발](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-JPA-%EC%9B%B9%EC%95%B1/dashboard)
- [스프링 부트와 AWS로 혼자 구현하는 웹 서비스](https://jojoldu.tistory.com/463)
- [Python으로 웹 스크래퍼 만들기](https://nomadcoders.co/python-for-beginners)
- [파이썬 무료 강의 (활용편3) - 웹 스크래핑 (5시간)](https://www.inflearn.com/course/%ED%8C%8C%EC%9D%B4%EC%8D%AC-%EC%9B%B9-%EC%8A%A4%ED%81%AC%EB%9E%98%ED%95%91/dashboard)

---
최종 수정 2021.08.29 © loosie
