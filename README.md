# ✅ Trello
**Trello** 는 웹기반의 프로젝트 관리 소프트웨어인 트렐로의 기술을 클론코딩한 기술입니다.

## 1️⃣ 프로젝트 소개
이 프로젝트는 사용자가 로그인을 하면 보드를 만들수 있고, 보드를 만든 사용자는  
그 보드안에서의 권한이 매니저가 되어 다른 사용자를 초대하고, 초대된 사용자들의 목록을 보며 프로젝트를 이끌어갈수 있습니다.  
해당 보드에 초대된 사용자만이 보드에 접은이 가능하며, 새로운 컬럼을 만들로, 그 컬럼의 위치를 옮길수 있습니다.  
그리고, 그 컬럼안에는 여러 목록들의 카드가 있고, 그 카드들 역시 하나의 컬럼안에서 위치를 옮길수도,  
다른 컬럼으로 위치를 옮겨가며 프로젝트 관리를 포스트잇 처럼 편하게 이용할수 있습니다.  

## 2️⃣ 팀 역할
| 역할            | 팀장 박다미                      | 팀원 김현민                                 | 장재현                          | 팀원 조성훈           |
|-----------------|----------------------------------|----------------------------------------------|--------------------------------------------|----------------------|
| 담당 기능       | - 회원관련 기능 <br> - 사용자 초대 <br> - 카드,컬럼이동 <br> - 카드,댓글 CRUD| - 로그아웃 <br> - 회원탈퇴 | - 컬럼CRUD| - 보드CRUD <br> 프론트엔드 |

## 3️⃣ 성능향상
N+1문제
카드 상세 정보보기 ->
카드 상세 정보를 조회할 때, 카드에 달린 댓글과 각 댓글의 작성자(User) 정보를 가져오는 과정에서 N+1 문제가 발생
해결 방법: 카드의 댓글 목록 조회 시 **댓글과 작성자 정보를 함께 페치 조인** 으로 가져와야 했습니다.

CardRepositoryImpl에서 findCommentsByCardId() 메서드에 페치 조인을 적용  
<img width="300" alt="image" src="https://github.com/user-attachments/assets/8a7a1ab0-2453-401e-989e-195e91fff76c">

> 이전 (N+1 문제 발생):
카드 조회 → 댓글 조회 → 각 댓글의 작성자(User) 개별 조회 (추가 쿼리 발생).  
댓글이 여러 개일수록 작성자 조회를 위한 추가적인 쿼리가 발생, N+1 문제로 이어짐.

> 이후 (페치 조인 적용, N+1 문제 해결):
카드 조회 → 댓글과 댓글 작성자(User) 함께 조회 (페치 조인).  
한 번의 쿼리로 댓글과 작성자 정보를 모두 가져와, 추가적인 쿼리가 발생하지 않음.

지연로딩으로 (게시판)1 + (댓글) 10+ (사용자)+10 = 21쿼리문 발생
이걸 페치조인으로 한번에 가져옴



 **기존 fetchCount() 사용** -> fetchOne() + select(qComment.count()) @BatchSize(size = 10)
 **Card에서 comment를 가져올때** -> 복합 인덱스사용

### 1. BoardRepositoryCustomImpl의 isBoardManager 메서드

**1-1)특정 보드의 매니저인지 확인**  
<img width="800" alt="image" src="https://github.com/user-attachments/assets/eea69147-25e9-452f-b123-cdb0b497fe03">  

**1-2) BoardRepositoryCustomImpl의 isBoardMember 메서드: 특정 보드의 사용자가 accepted인 member인지, 또 Manager인지**  
<img width="800" alt="image" src="https://github.com/user-attachments/assets/4c8f2bae-b6a7-468e-9096-73330a1cf315">  

**2.특정보드에 초대된 사용자인지, 사용자가 아닌지 로직**  
<img width="800" alt="image" src="https://github.com/user-attachments/assets/b007b30a-dbe6-4bdb-a901-4de4b681c67b">  

**3.카드 순서 관리 성능 문제 및 비정규화 도입**   
<img width="800" alt="image" src="https://github.com/user-attachments/assets/a354d877-5982-43d8-ab8d-a20974ecf890">  
각 카드가 자신의 위치를 개별적으로 관리하도록 설계했기 때문에, 카드 위치를 변경할 때마다 모든 카드를 업데이트해야 했기 때문에,   
모든 카드의 position 필드를 일일이 업데이트하는 방식은 데이터베이스 업데이트 횟수가 많을수록 성능이 저하되는 문제  
카드뿐 만 아니라 여러 요소에서 드래그 앤 드롭으로 위치를 수정하는 기능이 많기때문에  
하나의 위치데이터가 변경될 때 모든 데이터를 수정하는 방식보다, 성능면을 핵심적으로 처리해야 한다고 판단해서 비정규화  
-> 카드의 위치 정보를 **중앙에서 관리** 하게 한 결과, 카드 위치 변경 시 업데이트 작업의 양이 줄어들어 성능 향상

## 4️⃣ 동시성 제어
비관적 락과 낙관적 락 중 카드와 컬럼이동과 수정등 **피그마, 노션** 과 같이 동시에 접근할 가능성이 놓아서 **비관적 락** 선택  
-> 사용자 경험으로서의 **성능 저하** 와 **데드락** 가능성  
-> 트랜젝션의 격리수준을 **Read committed** 로 낮춰 속도를 높이고, **5초의 timeout**을 적용해서 데드락 해제  

## 5️⃣ ERD
<img width="900" alt="image" src="https://github.com/user-attachments/assets/f7110288-a2e4-40ed-be18-2486e5e78ff1">

## 🎇 시연
[유투브 시연 영상](https://www.youtube.com/watch?v=ptymlgRO5UI&t=213s)

## ⚡️ Jira Github 연동 및 규칙
[Jira](https://large-orchid-b96.notion.site/Jira-Github-d2caadb9f5474022b2c54a9030275e51)
