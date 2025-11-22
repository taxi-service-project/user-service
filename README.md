# MSA 기반 Taxi 호출 플랫폼 - User Service

Taxi 호출 플랫폼의 사용자 계정 관리 및 결제 수단 관리를 담당하는 마이크로서비스입니다.

## 주요 기능 (API Endpoints)

### 사용자 관리 (`/api/users`)

* `POST /`: 사용자 생성 (회원가입)
* `DELETE /{id}`: 사용자 삭제 (회원탈퇴)
* `PUT /{id}`: 사용자 정보 수정
* `GET /{id}`: 사용자 프로필 조회
* `PUT /{id}/password`: 사용자 비밀번호 변경

### 결제 수단 관리 (`/api/users/{userId}/payment-methods`)

* `POST /`: 결제 수단 등록
* `GET /`: 결제 수단 목록 조회
* `DELETE /{methodId}`: 결제 수단 삭제
* `PUT /{methodId}/default`: 기본 결제 수단 설정

## 기술 스택

* **Language & Framework:** Java, Spring Boot, Spring Web
* **Database:** Spring Data JPA, MySQL
