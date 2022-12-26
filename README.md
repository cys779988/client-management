## Database ERD

<img src="https://github.com/cys779988/help-me/blob/master/erd.PNG"/>

## API
프로젝트 실행 후 `http://localhost:8080/swagger-ui/index.html` 참조  
`https://github.com/cys779988/help-me/blob/master/api-docs.html` 참조

#### 회원가입 (`/user/signup`) POST

  
```
{
  "email": "hong1@naver.com",
  "password": "Helpme12@@",
  "name": "홍길동"
}
```  

#### 고객정보 등록 (`/customer`) POST
- 한국인 고객
  
```
{
  "name": "홍길동",
  "type": "KOREAN",
  "registNumber": "1564654-1534534",
  "email": "hong@naver.com",
  "address": "서울특별시 강남구 땡땡 0길 00",
  "contact": "010-1111-1111"
}
```  

- 외국인 고객

  
```
{
  "name": "홍길동",
  "englishName" : "hong",
  "type": "FOREIGN",
  "nationality" : "미국",
  "registDate": "1944-11-14",
  "email": "hong@naver.com",
  "address": "서울특별시 강남구 땡땡 0길 00",
  "contact": "010-1111-1111"
}
```  

- 한국법인

  
```
{
  "name": "홍길동",
  "type": "KOREAN_CORPORATION",
  "registNumber": "1564654-1534534",
  "email": "hong@naver.com",
  "address": "서울특별시 강남구 땡땡 0길 00",
  "contact": "010-1111-1111",
  "representive": [
    {
      "name": "이순신",
      "contact": "010-1111-1111"
    }
  ]
}
```  

- 외국법인

  
```
{
  "name": "홍길동",
  "englishName" : "hong",
  "type": "FOREIGN",
  "nationality" : "미국",
  "registDate": "1944-11-14",
  "email": "hong@naver.com",
  "address": "서울특별시 강남구 땡땡 0길 00",
  "contact": "010-1111-1111",
  "representive": [
    {
      "name": "이순신",
      "contact": "010-1111-1111"
    }
  ]
}
```  

#### 고객정보 수정 (`/customer/{id}`) PUT
유효성검증 파라미터는 고객정보 등록과 동일함.

  
```
// 한국법인 수정
{
  "name": "홍길동",
  "type": "KOREAN_CORPORATION",
  "registNumber": "1564654-1534534",
  "email": "hong@naver.com",
  "address": "서울특별시 강남구 땡땡 0길 00",
  "contact": "010-1111-1111",
  "representive": [
    {
      "name": "권율",
      "contact": "010-1111-1111"
    }
  ],
  "removeRepresentive": [
    1
  ]
}
```  

#### 고객 목록 기본정보 조회 (`/customer/basic`) GET

#### 고객 정보 조회 (`/customer/{id}`) GET


#### 고객 정보 삭제 (`/customer?id=`) DELETE

#### 제품 정보 등록 (`/product`) POST

  
```
{
  "name": "좋은제품",
  "quantity": 10,
  "price": 10000
}
```  

#### 제품 정보 수정 (`/product/{id}`) PUT

  
```
{
  "name": "좋은제품",
  "quantity": 10,
  "price": 10000
}
```  

#### 제품 정보 조회 (`/product?page=`) GET

#### 제품 정보 엑셀다운로드 (`/product/excel?page=`) GET

#### 제품 정보 삭제 (`/product?id=`) DELETE

#### 제품 판매 등록  (`/sales`) POST

  
```
{
  "customerId": 1,
  "productId": 1,
  "quantity": 10,
  "saleDate": "2022-12-23"
}
```  

#### 제품 판매 조회 (`/sales?page=`) GET

  
```
// 검색조건
{
  "customerName": "홍길동",
  "productName": "에어컨",
  "salesStartDate": "2022-10-21",
  "salesEndDate": "2022-12-21"
}
```  


#### 제품 판매 현황 엑셀다운로드 (`/sales/excel?page=`) GET

  
```
// 검색조건
{
  "customerName": "홍길동",
  "productName": "에어컨",
  "salesStartDate": "2022-10-21",
  "salesEndDate": "2022-12-21"
}
```  

## 문제해결방법

#### 관리자 계정 회원 가입
- 회원가입일시는 Spring Data JPA의 Audit 기능을 적용하여 자동으로 저장했습니다.
- 유효성 검사는 스프링 Validation을 적용했습니다.
- 비밀번호 단방향 암호화는 스프링 시큐리티의 BCryptPasswordEncoder 를 적용했습니다.

#### 관리자 로그인
- 스프링 시큐리티의 AuthenticationFailureHandler를 구현하여 ID, 비밀번호를 잘못 입력한 경우를 구분했습니다.
- 스프링 시큐리티 설정으로 로그인 성공 페이지, 로그인 페이지를 설정했습니다.
- HttpSessionRequestCache를 사용하여 request 정보를 가져와 로그인시 접속하려던 페이지로 리다이렉트 하도록 적용했습니다.

#### 고객 추가, 고객 수정
- 추상 엔티티로 고객의 공통 정보를 생성 후 고객타입마다 해당 부모 엔티티를 상속받아 각각의 엔티티를 생성했습니다. 상속 전략으로는 SINGLE_TABLE 을 사용했습니다.
- 고객 타입은 Enum으로 구분하였고 ConstraintValidator 을 구현하여 고객 타입별로 서로 다른 유효성 검증에 적용했습니다.
- 고객과 법인대표 엔티티는 연관관계를 활용하여 일대다 관계로 구성하였고 영속성 전이를 활용하여 고객 삭제 시 법인대표도 삭제될 수 있게 설정했습니다.

#### 제품 추가, 제품 정보 수정
- 유효성 검사는 스프링 Validation을 적용했습니다.
- 제품을 저장 시 제품명을 체크하도록 구현하였고 제품명을 유니크키로 설정했습니다.

#### 제품 판매 등록
- 멀티스레드 환경을 고려하여 제품 재고 수량 체크 로직에 낙관적 락을 활용했습니다.

#### 제품 종류 조회, 제품 판매 내역 조회
- 페이징 처리는 jpa의 페이징 기능을 활용했습니다. PageableHandlerMethodArgumentResolverCustomizer 인터페이스를 구현하여 기본 페이징 정보를 설정했습니다.
- 엑셀 다운로드는 `org.apache.poi` 라이브러리를 활용하여 구현했습니다. 엑셀다운로드 유틸함수를 만들어 활용했습니다.
- jpa Specification 인터페이스를 구현하여 다양한 조회 조건을 생성할 수 있게 적용했습니다.

#### 도커라이징
  
```
$ cd [프로젝트 루트 폴더]
$ ./gradlew bootJar
$ docker build -t help-me .
$ docker run --name help-me -d -p 8080:8080 help-me
```  
