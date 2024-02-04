# BookTalkBang
[서비스 링크](https://woogin.shop/booktalk)

## 프로젝트 소개
> 책과 관련해 중고거래가 메인인 사이트로 서로 책에 대해 추천하거나 이야기를 나눌 수 있는 서비스

## 팀 구성원 및 역할 분담
> 리더 이준영, 부리더 신승호, 팀원 김건우, 팀원 배형태

* 이준영
  * 채팅,알림,CI/CD 배포, 상품, 카테고리,캐싱
* 신승호
  * 회원가입, 로그인, 소셜로그인, 회원프로필,
* 김건우
  * 이미지 업로드,회원 차단, 상품 좋아요
* 배형태
  * 리뷰 게시글, 댓글, 좋아요, 회원 평점, 신고
* 공통사항
  * 프런트, 테스트코드 작성, 도커 실습

## 서비스 아키텍쳐
![Untitled](https://github.com/junyeong237/booktalk/assets/145661542/6e0342c6-add0-4271-b553-eb9d82604c73)

## ERD
![B08](https://github.com/junyeong237/booktalk/assets/145661542/53997058-e978-4099-acf4-def3a1ca24b4)


## 주요 기능
* 중고 거래 상품 등록
* 외부 상품과의 가격 비교 기능
* 거래를 위한 채팅기능
* 거래 후 판매자 평점 & 상품 리뷰기능
* 채팅과 리뷰를 위한 알림기능
* 회원 신고기능
* 백오피스 관리자 기능

## 사용 기술 스택
<details>
  <summary>BackEnd</summary>
 
* Java 17
* Spring boot 3.2.1
* Spring security 6.2.1
* JWT 0.11.5
* gradle 8.5
* QueryDSL 5.0.0
* spring data jpa 3.2.1
* spring data redis 3.2.1
* WebSocket 3.2.1
* STOMP
* KAKAO Open API
* Oauth 2.0
* junit5 5.10.1
* SSE
  
</details>

<details>
  <summary>FrontEnd</summary>
 
* HTML
* CSS
* JavaScript(jQuery)
* AJAX
* bootstrap 5.3.0
* thymeleaf 3.1.2
  
</details>

<details>
  <summary>DB</summary>
 
* MySQL 8.2.0
* Redis
* h2 2.2.224
  
</details>

<details>
  <summary>infra</summary>
 
* AWS
  * EC2
  * S3
  * RDS
  * CodeDeploy
* GitHub Actions
  
</details>

<details>
  <summary>Docs</summary>
 
* Jmeter 5.6.3

</details>

## 기술적 의사결정

<details>
  <summary>Repsitory의존 + default Method 사용</summary>
 
* 다른 서비스 도메인에서 다른 서비스를 호출하는 방식보단 레포지토리에 의존하는 방식 선택
* 이 경우 중복 메서드를 매번 작성해줘야하지만 이를 방지하기 위해 레포지토리단에서 디폴트 메서드를 사용해 중복성 제거
  
</details>

<details>
  <summary>WebSocket&Stomp 사용</summary>
 
* 채팅시스템을 위해선 클라이언트와 서버간의 양방향 통신을 제공받아야함
* 따라서 Http 통신과 다르게 연결을 맺고 바로 끊어버리는게 아닌 계속 유지할 수 있는WebSocket선택
* STOMP는 웹소켓과 함께 사용되는 메시징 프로토콜로 WebSocket만 사용했을때 일일히 handler를 수동작성해줘야했던것과 달리 STOMP를 웹소켓과 같이 사용하여 비교적 쉬운 초기설정과 관리가 가능한 pub/sub구조의 체계적인 응답형식을 가지고있다.
  
</details>

<details>
  <summary>Redis Cache 효율성 문제</summary>
 
* 같은 쿼리문이 쓸데없는 반복되는것을 피하기 위해 캐싱 사용
* 하지만 redis에 데이터가 쌓이는것 자체가 메모리 낭비로 이어질 수 있는 가능성이 있음
* 따라서 적절한 trade-off를 고려하여 레디스 캐싱의 시간을 줄이고 쿼리문 최적화가 필수적
  
</details>

<details>
  <summary>프런트 배포 방식 - 타임리프 & Ajaxs</summary>
 
* 타임리프를 사용하여 서버에서 동적으로 HTML을 생성하고 비동기적으로 데이터를 처리할 수 있는 ajax를 사용
* 따라서 모노리틱 아키텍쳐기반으로 프런트와 백엔드를 같이배포하는 방식선택
  
</details>

<details>
  <summary>프런트 배포 방식 - 타임리프 & Ajaxs</summary>
 
* jwt
  * 사용자의 로그인 상태정보를 클라이언트에 저장하기 때문에 서버의 부담감소
  * 서버 간 토큰을 공유하거나 검증할 필요가 없기 때문에 확장성 보장
* Refresh Token
  * AccessToken의 사용주기를 짧게 하여 보안을 강화함과 동시에 사용자에게 잦은 로그인 경험을 주지 않기 위해 사용
  * 인메모리DB인 레디스를 활용해 적은 메모리사용과 빠른 작성으로 Refresh Token을 저장
  * 레디스의 TTL을 통해 RefreshToken 사용주기 관리가 용이
  * key value 형태로 RefreshToken과 user_id를 저장
* authentification filter vs userService에 직접 구현
  * 실제적으로 다른 api에서 요구되는 인가 상태는 authoriaztion filter를 통해 모듈화된 상태
  * 소셜로그인, 차단유저와 탈퇴유저의 로그인 제한 등 인증매커니즘의 유연성 필요로 직접 구현 결정
    
</details>
