# 👤 User Service (Identity)

> **사용자/기사 회원가입, 로그인(토큰 발급), 내 정보 및 결제 수단 관리를 담당합니다.**

## 🛠 Tech Stack
| Category | Technology                         |
| :--- |:-----------------------------------|
| **Language** | Java 17                            |
| **Framework** | Spring Boot (MVC), Spring Security |
| **Database** | MySQL (JPA)                        |
| **Auth** | JWT (Access/Refresh), Cookie       |

## 📡 API Specification

### Authentication
| Method | URI | Auth | Description |
| :--- | :--- | :---: | :--- |
| `POST` | `/login` | ❌ | 로그인 (Access Header + Refresh Cookie 발급) |
| `POST` | `/reissue` | ❌ | 토큰 재발급 (RTR 적용) |

### User Management
| Method | URI | Auth | Description |
| :--- | :--- | :---: | :--- |
| `POST` | `/api/users` | ❌ | 일반 사용자 회원가입 |
| `GET` | `/api/users/{id}` | 🔐 | 내 프로필 조회 (본인 확인) |
| `PUT` | `/api/users/{id}` | 🔐 | 내 정보 수정 (본인 확인) |
| `DELETE` | `/api/users/{id}` | 🔐 | 회원 탈퇴 (본인 확인) |
| `PUT` | `/api/users/{id}/password` | 🔐 | 비밀번호 변경 (본인 확인) |

### Payment Methods
| Method | URI | Auth | Description     |
| :--- | :--- | :---: |:----------------|
| `POST` | `/api/users/{id}/payment-methods` | 🔐 | 결제 수단 등록        |
| `GET` | `/api/users/{id}/payment-methods` | 🔐 | 결제 수단 목록 조회     |
| `DELETE` | `/api/users/{id}/payment-methods/{methodId}` | 🔐 | **결제 수단 삭제**    |
| `PUT` | `/api/users/{id}/payment-methods/{methodId}/default` | 🔐 | **기본 결제 수단 설정** |

## 🚀 Key Improvements
* **RTR & Cookie Security:** Refresh Token을 `HttpOnly`, `SameSite=Strict` 쿠키에 저장하고 재발급 시 교체(Rotate)하여 보안 강화.
* **IDOR 방어:** `validateOwner` 로직을 통해 헤더의 ID와 대상 ID를 대조하여 권한 도용 차단.
* **JPA 최적화:** `Fetch Join`으로 N+1 문제 해결 및 벌크 연산 최적화.