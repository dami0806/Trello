# 🗓️ Trello 

<img width="1000" alt="image" src="https://github.com/user-attachments/assets/cac6d0d3-a200-41d8-85fe-f5333b51dbba" />


<img width="800" alt="image" src="https://github.com/user-attachments/assets/fb0e54fc-7bbb-4635-83f5-aa259fd0ca44" />

## 📌 프로젝트 개요
- **프로젝트 기간**: 2024.06.03 ~ 2024.06.10
- **프로젝트 설명**: 칸반 보드 기반의 프로젝트 관리 시스템 개발

---

## 기술 스택 백엔드  
<div align=left>
    <img src="https://img.shields.io/badge/JDK%2021-E38836?style=flat-square&logo=openJdk&logoColor=white" style="margin-right: 2px;">
    <img src="https://img.shields.io/badge/Spring%20Boot%203.4.2-6DB33F?style=flat-square&logo=springboot&logoColor=white" style="margin-right: 2px;">
    <img src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=flat-square&logo=spring&logoColor=white">
</div>

<div align=left>
     <img src="https://img.shields.io/badge/QueryDSL%20Compose-2496ED?style=flat-square&logo=docker&logoColor=white">
     <img src="https://img.shields.io/badge/Jpql-6DB33F?style=flat-square&logo=spring&logoColor=white" style="margin-right: 2px;">
    <img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white" style="margin-right: 2px;">
</div>

<div align=left>
    <img src="https://img.shields.io/badge/MapperStruct-6DB33F?style=flat-square&logo=spring&logoColor=white" style="margin-right: 2px;">
    <img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=gradle&logoColor=white">
</div>

---

### 데이터베이스  
<div align=left>
    <img src="https://img.shields.io/badge/MySQL%208-08668E?style=flat-square&logo=mysql&logoColor=white" style="margin-right: 2px;">
    <img src="https://img.shields.io/badge/Redis-FF0000?style=flat-square&logo=redis&logoColor=white">
</div>


### 테스트  
<div align=left>
    <img src="https://img.shields.io/badge/JUnit%205-25A162?style=flat-square&logo=junit5&logoColor=white" style="margin-right: 2px;">
    <img src="https://img.shields.io/badge/JMeter-D22128?style=flat-square&logo=apachejmeter&logoColor=white" style="margin-right: 2px;">
    <img src="https://img.shields.io/badge/Jacoco-6DB33F?style=flat-square">
</div>


### 모니터링  
<p align="left">
    <img src="https://img.shields.io/badge/Prometheus-E6522C?style=flat-square&logo=prometheus&logoColor=white" style="margin-right: 2px;">
    <img src="https://img.shields.io/badge/Grafana-F46800?style=flat-square&logo=grafana&logoColor=white" style="margin-right: 2px;">
    <img src="https://img.shields.io/badge/Grafana%20Loki-FF9900?style=flat-square&logo=grafana&logoColor=white">
</p>

### 문서  
<div align=left>
    <img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=swagger&logoColor=black">
</div>


### 인프라  
<div align=left>
    <img src="https://img.shields.io/badge/AWS%20EC2-FF9900?style=flat-square&logo=amazonec2&logoColor=white" style="margin-right: 2px;">
    <img src="https://img.shields.io/badge/AWS%20RDS-527FFF?style=flat-square&logo=amazonrds&logoColor=white" style="margin-right: 2px;">
</div>

<div align=left>
    <img src="https://img.shields.io/badge/AWS%20S3-44B8A1?style=flat-square&logo=amazonaws&logoColor=white" style="margin-right: 2px;">
    <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white" style="margin-right: 2px;">
</div>


---
## 🔧 아키텍처 구성
[아키텍처 다이어그램 필요]

---

## 🚀 주요 기능
- **사용자 관리**: 회원가입, 로그인, 권한 부여 (Manager, Member)
- **보드 관리**: 보드 생성, 초대, 초대할 사용자 목록 조회
- **컬럼 관리**: 컬럼 추가, 이동, 삭제
- **카드 관리**: 카드 추가, 이동, 삭제, 댓글
- **권한 관리**: AOP 기반 접근 제어 및 Role 기반 인증 처리

## 팀 역할
| 역할            | 팀장 박다미                      | 팀원 김현민                                 | 장재현                          | 팀원 조성훈           |
|-----------------|----------------------------------|----------------------------------------------|--------------------------------------------|----------------------|
| 담당 기능       |회원관련 기능 <br> 사용자 초대 <br> 카드,컬럼이동 <br> 카드,댓글 CRUD| 로그아웃 <br> 회원탈퇴 | 컬럼CRUD| 보드CRUD <br> 프론트엔드 |

---

## ⚡️ 성능 개선

### **카드 순서 관리 및 성능 최적화**  
**기존 방식의 문제점**  
 - 각 카드가 개별적으로 위치를 관리 → 모든 카드의 위치를 업데이트해야 하는 성능 저하 발생  
  - 대량의 데이터가 수정될 경우, 데이터베이스 부하 증가  

**개선 방식**  
 - **중앙에서 카드 위치를 관리** → 불필요한 업데이트 최소화  
  - **트랜잭션 단위 최적화** → 변경 사항이 있을 때만 업데이트 수행  

| 개선 후          | 개선 후                     |
|-----------------|----------------------------------|
|<img width="482" alt="image" src="https://github.com/user-attachments/assets/801c8bc9-8621-4b49-9b33-7cb23fd6b8c0" /> | <img width="445" alt="image" src="https://github.com/user-attachments/assets/23a7c0e1-c103-4f06-a45f-e71059f4e9cd" />  |

###  **쿼리 최적화 및 인덱싱 적용**
- **기존 fetchCount() 사용** → fetchOne() + select(qComment.count()) @BatchSize(size = 10)
- **N+1 문제 해결**: Fetch Join, EntityGraph, BatchSize 적용
- **복합 인덱스 사용**: 카드, 댓글, 컬럼 관련 조회 최적화

**쿼리 최적화 개요**  
- fetchCount() 대신 fetchOne() + count()를 활용하여 필요 없는 데이터 조회 감소  
- N+1 문제를 해결하기 위해 Fetch Join 및 BatchSize를 활용  
- 카드 및 컬럼 이동 시 복합 인덱스를 적용하여 검색 속도 개선  

### sql로 인한 성능 개선(인덱스 사용 및 sql개선후 explain으로 확인)
| 설명          | explain 개선결과                     |
|-----------------|-----------------
|**매니저 확**인 |<img width="600" alt="image" src="https://github.com/user-attachments/assets/eea69147-25e9-452f-b123-cdb0b497fe03"> |
|**member인지/ Manager인지 확인** |<img width="700" alt="image" src="https://github.com/user-attachments/assets/4c8f2bae-b6a7-468e-9096-73330a1cf315">|
| **초대된 사용자인지 여부 확인** |<img width="700" alt="image" src="https://github.com/user-attachments/assets/b007b30a-dbe6-4bdb-a901-4de4b681c67b"> |
|**카드 순서 관리 성능 문제 및 비정규화 도입** |<img width="700" alt="image" src="https://github.com/user-attachments/assets/a354d877-5982-43d8-ab8d-a20974ecf890">  |

---

## 🔍 트러블슈팅 및 고민
### **동시성 문제 해결**
- **문제**: 여러 스레드가 동시에 데이터를 수정할 경우, 트랜잭션 충돌 발생
- **해결책**: StampedLock을 활용하여 읽기 성능 최적화 및 충돌 최소화

| 개선 후          | 개선 후                     |
|-----------------|----------------------------------|
| <img width="500" alt="image" src="https://github.com/user-attachments/assets/489fdd27-197a-4242-b46c-9cfbf14c6233" />      | <img width="600" alt="image" src="https://github.com/user-attachments/assets/f70d4ac1-ea00-4c6d-b496-2877eebb4f3a" />  |

### **트랜잭션 충돌 최소화**
- **이전 방식**: `@Version` 기반 낙관적 락 사용 → 읽기 중 변경 감지 어려움
- **개선 방식**: StampedLock 적용하여 읽기 중 데이터 변경 감지 및 안전한 재처리

---

## 📂 ERD
<img width="882" alt="image" src="https://github.com/user-attachments/assets/2152662b-a680-43f1-8463-69ed6eb04674" />


---

## 🎥 시연 영상
[유튜브 시연 영상](https://www.youtube.com/watch?v=ptymlgRO5UI&t=213s)

---

## 📌 프로젝트 관리
- **이슈 관리**: Jira를 활용한 GitHub 연동
- **코드 스타일**: Java 코드 컨벤션 준수, MapperStruct 활용하여 가독성 향상  
[🔗 Jira link](https://large-orchid-b96.notion.site/Jira-Github-d2caadb9f5474022b2c54a9030275e51)

---

## 📚 추가 문서 및 참고 링크
- 🔗 [동시성 문제 해결 연구](https://github.com/dami0806/Trello/wiki/%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0%EC%9D%98-%EC%B5%9C%EC%A0%81-%EC%86%94%EB%A3%A8%EC%85%98-%ED%83%90%EA%B5%AC)
- 🔗 [SQL을 통한 성능 최적화](https://github.com/dami0806/Trello/wiki/%EC%BF%BC%EB%A6%AC-%EC%B5%9C%EC%A0%81%ED%99%94-%EA%B0%9C%EB%85%90)
- 🔗 [Fetch Join, EntityGraph, BatchSize 비교 및 활용](https://github.com/dami0806/Trello/wiki/Fetch-Join,-EntityGraph,-BatchSize-%EB%B9%84%EA%B5%90-%EB%B0%8F-%ED%99%9C%EC%9A%A9)
- 🔗 [MapperStruct 라이브러리 활용](https://github.com/dami0806/Trello/wiki/MapperStruct-%EB%9D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC)
