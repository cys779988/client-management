## Database ERD

<img src="https://github.com/cys779988/help-me/blob/master/erd.PNG"/>

## API

1. 프로젝트 실행 후 `http://localhost:8080/swagger-ui/index.html` 참조  
2. `https://github.com/cys779988/help-me/blob/master/api-docs.html` 참조

## 문제해결방법

#### 관리자 계정 회원 가입
- 회원가입일시는 Spring Data JPA의 Audit 기능을 적용하여 자동으로 저장했습니다.
- 유효성 검사는 스프링 Validation을 적용했습니다.
- 비밀번호 단방향 암호화는 스프링 시큐리티의 BCryptPasswordEncoder 를 적용했습니다.

#### 관리자 로그인
- 스프링 시큐리티의 AuthenticationFailureHandler를 구현하여 ID, 비밀번호를 잘 못 입력한 경우를 구분했습니다.
- 스프링 시큐리티 설정으로 로그인 성공 페이지, 로그인 페이지를 설정했습니다.
- HttpSessionRequestCache를 사용하여 request 정보를 가져와 로그인시 접속하려던 페이지로 리다이렉트 하도록 적용했습니다.

#### 고객 추가, 고객 수정
- 추상 엔티티로 고객의 공통 정보를 생성 후 고객타입마다 해당 부모 엔티티를 상속받아 각각의 엔티티를 생성했습니다. 상속 전략으로는 SINGLE_TABLE 을 사용했습니다.
- 고객 타입은 Enum으로 구분하였고 ConstraintValidator 을 구현하여 고객 타입별로 서로 다른 유효성 검증을 구현했습니다.
- 고객과 법인대표 엔티티는 연관관계를 활용하여 일대다 관계로 구성하였고 영속성 전이를 활용하여 고객 삭제 시 법인대표도 삭제될 수 있게 설정했습니다.

#### 제품 추가, 제품 정보 수정
- 유효성 검사는 스프링 Validation을 적용했습니다.
- 제품을 저장 시 제품명을 체크하도록 구현하였고 제품명을 유니크키로 설정했습니다.

#### 제품 종류 조회, 제품 판매 내역 조회
- 페이징 처리는 jpa의 페이징 기능을 활용했습니다. PageableHandlerMethodArgumentResolverCustomizer 인터페이스를 구현하여 기본적인 페이징 정보를 설정했습니다.
- 엑셀 다운로드는 `org.apache.poi` 라이브러리를 활용하여 구현했습니다.
- jpa Specification 인터페이스를 구현하여 다양한 조회 조건을 생성할 수 있게 적용했습니다.
