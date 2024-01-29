# Daily Wrap Up

## 20240129

### 오늘 한 것

- 경배님과 백엔드 merge
- merge 후 에러 수정
- 패키지 구조 수정
- 코드 스타일 정함
  - 엔티티는 builder 사용
  - 서비스는 인터페이스, impl사용
  - 컨트롤러에 있는 유효성 검사 전부 서비스에서 하기
  - 유저 정보는 전부 jwt로 받아오기
- 롤링페이퍼 s3올리는 것 성공
- redis에 연결하려 했으나 실패

### 어려웠던 점

- merge 후 컨벤션 정하는 데 시간이 오래걸림
- redis에 객체를 리스트로 넣고 싶은데 되지를 않는다...

### 새로 알게 된 점

- builder에 대해 새로 알게 됨
- jpa entity @AllArgsConstructor 사용 가넝(나는 루프에 빠지는 줄 알았음)

### 내일 할 것

- redis 연결
- 소그룹 session 열기
- 배포

## 20240127-28

### 주말동안 한 것

- S3 IAM 계정 생성
- S3로 업로드하는 Event API 생성

### 어려웠던 점

- Multipart/file과 json을 같이 받는 것
  - `@RequestPart`를 사용하면 된다.
  - 특이점은 userId하나만 json에서 받고 싶더라도 dto로 감싸줘야 인식을 했음
- application-s3.yml에 S3 설정 내용을 담았는데 안됨...
  - `Could not resolve placeholder 'cloud.aws.credentials.access-key' in value "${cloud.aws.credentials.access-key}"`
  - 그래서 그냥 application.properties에 담으니까 됐음

### 내일 할 것

- Event API 끝내기
- redis에 연결
- 소그룹 session 생각하기

## 20240126

### 전날 저녁에 한 것

- OpenVidu on-premise 방식으로 배포 시도. 서버가 열려있지 않아 실패

### 오늘 한 것

- 3주차 발표 경청
- OpenviduController 에러 처리 후 서비스로 에러처리하라는 코드리뷰 받아서 수정

### 어려웠던 점

- 배포할 때 port번호 설정에 어려움

### 새로 알게 된 점

- 예외처리는 controller가 아닌 service단에서 한다
- server port, http_port, https_port는 전부 다르다

### 주말에 할 것

- OpenVidu 배포
- redis와 연결
- 채팅 알아보기

## 20240125

### 오늘 한 것

- openvidu controller 뼈대 완성(exception 처리 추가 필요)

### 내일 할 것

- 서버에 업로드
- exception 컨트롤러에 추가
- 카카오페이 api 추가
- 소그룹 어떻게 하냐,,생각하기
- 채팅 한번 구경하기
- 설문조사 메서드 생성

## 20240124

### 오늘 한 것

- openvidu session 생성 repo, sevice 성공
- Event, Result 테이블 정규화 후 entity 생성
- maven 3.9.0 설치
- openvidu 커스텀 시작

### 어려웠던 점

- 오픈비두 튜토리얼을 커스텀하려고 하니까 되지 않아서 docker에서 pull해와서 커스텀 시작
- maven 설치해야하는 줄 모르고 mvn 왜 안되나 했음

### 새로 알게 된 점

- JPA entity에서 enum column은 기본값을 주려면 하드코딩하는 법 밖에 없다
- - Intellij 글자 오류
    - `Alt+=` 하면 알파벳이 하나하나 떨어져서 나옴

### 내일 할 것

- OpenVidu 컨트롤러 만들기

## 20240123

### 오늘 한 것

- openvidu session post 연결 성공
- event controller, repo, service 생성
- gerrit, 컨플릭과 기나긴 싸움…………………………………………………

### 어려웠던 점

- 엔티티 수정 때 오류가 남
- h2 db는 user라는 테이블명을 가질 수 없다. 이것때문에 2시간 날림
- dto 생성이 어렵다

### 내일 할 것

- OpenVidu 컨트롤러, 서비스, 레포 끝내기

## 20240122

### 오늘 한 것

- Entity 생성

### 어려웠던 점

- 양방향, 단방향 설정을 뭐로 해야할지 헷갈렸음

### 내일 할 것

- OpenViduController 완성

## 20240120-21

### 주말동안 한 것

- redis 드디어 연결
- OpenVidu api 명세서 작성

### 어려웠던 점

- entityManagerFactory를 Bean으로 못 만든 오류
  - h2가 꺼져있어서 그랬다...^^
- redisConfig를 Bean으로 못 만든 오류

```java
@Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;
```

이걸 `@Value("${spring.data.redis.host}")` 이렇게 했어야 했음!!!!!!! yml을 무시하지 말자..
여기서 끝이 아님

- org.hibernate.exception.sqlgrammarexception: could not prepare statement [sequence "hello_seq" not found; sql statement:

```yml
jpa:
  hibernate:
    ddl-auto: create
```

여기서 jpa: 를 안해서 ddl-auto가 적용이 안돼서 sequence를 생성하기 못했던 것임;;;;

### 내일 할 것

- Entity 생성
- OpenVidu 시작

## 20240119

### 오늘 한 것

- 패키지 생성
- docker 설치
- redis 공부
- redis config 실패

### 어려웠던 점

- redis 연결을 할 때 redisConfig가 계속 연동이 되지 않음... 계속 entityManagerFactory가 없다고 뜸...

### 주말동안 할 일

- 초기 세팅 완료
- OpenVidu 작업 시작

## 20240118

### 오늘 한 것

- 피그마 만들기
- api 명세서 아주 조금 수정
- 브랜치 생성
- 백엔드 프로젝트 생성

### 어려웠던 점

- api response body parameter에 대한 이해
- 리모트 브랜치를 로컬 브랜치로 가져오기

### 내일 할 것

- redis, querydsl 초기 셋팅
- webRTC 공부
- webRTC 생성

## 20240117

### 오늘 한 것

- 기술 스택 정하기
- 기능 담당 정하기
- 컨설턴트님 피드백
- 쿼리 DSL 공부

### 어려웠던 점

- 없음

### 내일 할 것

- 피드백 회의(밥 먹기 전 끝내보기)
- 와이어프레임 생성
- 피그마 수정
- 명세서 수정
- api 명세서 수정
  - 간단한 규칙에 맞게 수정
- erd 수정

## 20240116

### 오늘 한 것

- ERD 피드백
- API 명세서 작성
- Jira 공부(너무 어려움 미쳤음)
  - 지라 규칙 생성
  - 스토리 포인트 생성 배움

### 어려웠던 점

- 지라가 아직 헷갈린다...

### 내일 할 것

- 기술 스택 정하기
- 기능 별 파트 분배
- 기획 마무리
- 도커 공부
- S3 공부
- Redis 공부
- 쿼리 DSL

## 20240115

### 주말동안 한 것

- 화상 화면 피그마 완성

### 오늘 한 것

- 피그마 완성
  - 피그마 피드백 받은 것 수정
- 개발 컨벤션 정리
- 지라 깃랩 연동

### 어려웠던 점

- 컨벤션을 정리할 것이 많아서 헷갈림

### 내일 할 것

- api 명세 작성
- 도커 공부
- S3 공부
- Redis 공부
- 쿼리 DSL

## 20240112

### 오늘 한 것

- 피그마 완성
  - 소셜 로그인
  - 화상 모바일 화면
  - 소그룹
  - 송금, 메시지 작성 모달
- 피그마 피드백 받기
- 기획서 피드백하기

### 어려웠던 점

### 주말에 할 것

- 도커 공부
- 지라 공부
- 깃랩 공부
- S3 공부
- Redis 공부
- 쿼리 DSL

## 20240111

### 오늘 한 것

피그마팀, 기획서팀을 나눠서 진행. 나는 피그마팀.

- 완성
  - flow chart
  - 화상 메인
  - 소그룹
  - 송금, 메시지 작성 모달
- 진행 중
  - 소셜 로그인 모달

### 내일 할 것

## 20240110

- 규칙 정함
  - 깃 컨벤션
  - 파트 나누기
  - 기술스택
  - 그라운드룰 추가
  - 기능 리스트업
- 디자인 컨셉
  - 색상
  - 폰트
  - 레이아웃

## 20240109

- 피그마 목업
- 기획서 작성

## 20240108

- 기능 리스트업
- 와이어 프레임 작성
- 디자인 시스템 생성
