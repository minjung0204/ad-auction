### **네이버 플레이스 상위노출 입찰 사이트 MVP 개발 체크리스트**

**참고:**

- 프론트엔드와 백엔드 개발은 병렬로 진행될 수 있습니다. 본 체크리스트는 논리적 의존성을 고려한 순서입니다.
- 각 커밋 메시지는 예시이며, 실제 커밋 시 더 상세한 내용을 포함할 수 있습니다.
- 주석은 한국어로 작성하도록 지시사항을 반영합니다.

---

**1. 초기 설정 및 환경 구축 (Initial Setup & Environment Configuration)**

- [ ] **작업 내용:** 프로젝트 저장소 초기화 및 기본 폴더 구조 설정 (`backend`, `frontend`, `docs` 등).
  - 백엔드 (Spring Boot) 프로젝트 생성 및 초기 설정 (Maven/Gradle, Spring Web 등).
  - 프론트엔드 (React) 프로젝트 생성 및 초기 설정 (Vite/CRA).
  - MySQL 데이터베이스 인스턴스 준비 및 연결 정보 설정.
  - Git 저장소 초기화 및 `.gitignore` 파일 설정.
- **확인 지점:**
  - 각 프로젝트가 정상적으로 실행되는가?
  - 데이터베이스 연결 설정이 올바른가?
  - Git 커밋 및 푸시가 가능한가?
- **커밋 지점:** 프로젝트 초기 설정 및 기본 환경 구축 (`feat: 프로젝트 초기 설정 및 환경 구축`)

---

**2. 데이터베이스 설계 및 구현 (Database Design & Implementation)**

- [ ] **작업 내용:** `design_document.md`의 데이터베이스 설계 섹션을 기반으로 테이블 스키마 정의 및 DDL 스크립트 작성.
  - `User` 테이블 생성.
  - `Bid` 테이블 생성 (FK: `User.user_id` - `advertiser_id`, `selected_agency_id`).
  - `Proposal` 테이블 생성 (FK: `Bid.bid_id`, `User.user_id` - `agency_id`).
  - `Payment` 테이블 생성 (FK: `Bid.bid_id`, `User.user_id` - `advertiser_id`, `agency_id`).
  - `Settlement` 테이블 생성 (FK: `Payment.payment_id`, `User.user_id` - `agency_id`).
  - `Review` 테이블 생성 (FK: `User.user_id` - `advertiser_id`, `agency_id`, `Bid.bid_id`).
  - `Portfolio` 테이블 생성 (FK: `User.user_id` - `agency_id`).
  - `Notification` 테이블 생성 (FK: `User.user_id`).
  - `ChatRoom` 테이블 생성 (FK: `Bid.bid_id`, `User.user_id` - `advertiser_id`, `agency_id`).
  - `ChatMessage` 테이블 생성 (FK: `ChatRoom.chat_room_id`, `User.user_id` - `sender_id`).
- **확인 지점:**
  - 모든 테이블이 `design_document.md`에 명시된 필드, 데이터 타입, 제약조건에 맞춰 생성되었는가?
  - 외래 키(FK) 관계가 올바르게 설정되었는가?
  - `AUTO_INCREMENT`, `NOT NULL`, `UNIQUE` 등 제약조건이 정확하게 적용되었는가?
- **커밋 지점:** 데이터베이스 테이블 DDL 스크립트 작성 (`feat: 데이터베이스 초기 스키마 정의 및 DDL`)

---

**3. 백엔드 핵심 기능 구현 (Backend Core Feature Implementation)**

- **3.1 사용자 (User) API**

  - [ ] **작업 내용:** `User` 엔티티, DTO, Repository, Service, Controller 클래스 생성.
    - `POST /api/v1/users/register`: 회원가입 (이메일, 소셜) 구현.
    - `POST /api/v1/users/login`: 로그인 구현.
    - JWT 기반 인증 및 인가(Authentication & Authorization) 기본 설정.
  - **확인 지점:**
    - 회원가입 및 로그인 기능이 정상적으로 동작하는가?
    - 유효한 JWT 토큰이 발급되는가?
    - Spring Security 설정이 올바르게 적용되었는가?
  - **커밋 지점:** 사용자 인증 및 회원가입 API 구현 (`feat: 사용자 인증 및 회원가입 API 구현`)
  - [ ] **작업 내용:**
    - `GET /api/v1/users/me`: 내 정보 조회 구현.
    - `PUT /api/v1/users/me`: 내 정보 수정 구현.
    - 비밀번호 해싱 (`BCryptPasswordEncoder` 등) 적용.
  - **확인 지점:**
    - 로그인 후 내 정보 조회 및 수정이 정상적으로 동작하는가?
    - 비밀번호가 안전하게 저장되고, 수정 시에도 해싱이 적용되는가?
  - **커밋 지점:** 사용자 정보 조회 및 수정 API 구현 (`feat: 사용자 정보 관리 API 구현`)

- **3.2 광고주 (Advertiser) API**

  - [ ] **작업 내용:** `Bid` 엔티티, DTO, Repository, Service, Controller 클래스 생성.
    - `POST /api/v1/advertisers/bids`: 입찰 요청 등록 API 구현.
    - 동일한 플레이스 링크 및 키워드 조합에 대한 중복 입찰 요청 불가 로직 구현.
  - **확인 지점:**
    - 새로운 입찰 요청이 정상적으로 등록되고 데이터베이스에 저장되는가?
    - 중복 입찰 요청 시 적절한 오류 응답을 반환하는가?
  - **커밋 지점:** 광고주 입찰 요청 등록 API 구현 (`feat: 광고주 입찰 요청 등록 API 구현`)
  - [ ] **작업 내용:**
    - `GET /api/v1/advertisers/bids`: 내 입찰 요청 목록 조회 API 구현.
    - `GET /api/v1/advertisers/bids/{bid_id}`: 특정 입찰 요청 상세 조회 API 구현.
  - **확인 지점:**
    - 광고주가 본인의 입찰 목록 및 상세 내용을 올바르게 조회할 수 있는가?
  - **커밋 지점:** 광고주 입찰 조회 API 구현 (`feat: 광고주 입찰 목록/상세 조회 API 구현`)

- **3.3 대행사 (Agency) API**
  - [ ] **작업 내용:** `Proposal` 엔티티, DTO, Repository, Service, Controller 클래스 생성.
    - `GET /api/v1/agencies/bids`: 전체 입찰 요청 목록 조회 API 구현 (광고주 정보 블라인드 처리).
    - `POST /api/v1/agencies/bids/{bid_id}/proposals`: 입찰 제안 제출 API 구현.
    - 제안 제출 후 1회 수정 가능 로직 구현.
  - **확인 지점:**
    - 대행사가 현재 진행 중인 모든 입찰 요청 목록을 조회할 수 있는가?
    - 제안 제출 및 1회 수정 기능이 올바르게 동작하는가?
  - **커밋 지점:** 대행사 입찰 제안 API 구현 (`feat: 대행사 입찰 제안 및 목록 조회 API 구현`)

---

**4. 프론트엔드 핵심 기능 구현 (Frontend Core Feature Implementation)**

- **4.1 공통 UI/UX**

  - [ ] **작업 내용:** 메인 페이지, 회원가입/로그인 페이지 구현.
    - 이메일 및 소셜 로그인(네이버, 카카오) 연동을 위한 UI 구현.
    - 추가 회원 정보(사업자등록번호, 상호명 등) 입력 폼 구현.
  - **확인 지점:**
    - 회원가입 및 로그인 페이지가 정상적으로 렌더링되고, 사용자 입력이 가능한가?
    - 소셜 로그인 버튼 클릭 시 해당 서비스로 리다이렉트 되는가?
  - **커밋 지점:** 회원가입/로그인 UI 구현 (`feat: 회원가입 및 로그인 페이지 UI 구현`)

- **4.2 광고주 화면**

  - [ ] **작업 내용:**
    - '입찰 요청 등록' 페이지 UI 구현 (플레이스 링크, 희망 노출 순위, 키워드 입력 필드, 등록 버튼).
    - `POST /api/v1/advertisers/bids` API 연동 및 성공/실패 피드백 구현.
    - 내 입찰 목록 및 상세 페이지 UI 구현.
  - **확인 지점:**
    - 입찰 요청 등록 폼이 정상적으로 렌더링되고, 데이터 입력 및 제출 시 API 연동이 잘 되는가?
    - 등록 후 내 입찰 목록에 새로 등록된 입찰이 보이는가?
    - 각 입찰의 상세 내용이 올바르게 표시되는가?
  - **커밋 지점:** 광고주 입찰 요청 및 조회 UI 구현 (`feat: 광고주 입찰 요청 및 조회 UI 구현`)

- **4.3 대행사 화면**
  - [ ] **작업 내용:**
    - 입찰 요청 목록 페이지 UI 구현 (모든 입찰 요청 표시, 광고주 정보 블라인드 처리).
    - 특정 입찰에 대한 제안 가격 입력 필드 및 제출/수정 버튼 UI 구현.
    - `POST /api/v1/agencies/bids/{bid_id}/proposals` API 연동.
  - **확인 지점:**
    - 대행사 로그인 후 전체 입찰 목록이 올바르게 표시되는가?
    - 제안 제출 및 수정 UI가 정상적으로 동작하며 API 연동이 잘 되는가?
  - **커밋 지점:** 대행사 입찰 목록 및 제안 UI 구현 (`feat: 대행사 입찰 목록 및 제안 UI 구현`)

---

**5. 추가 기능 구현 및 연동 (Additional Feature Implementation & Integration)**

- **5.1 결제, 정산, 리뷰, 포트폴리오 기능 (Backend & Frontend)**

  - [ ] **작업 내용:**
    - 광고주: `PUT /api/v1/advertisers/bids/{bid_id}/select`: 대행사 선정 API 구현 및 UI 연동.
    - 광고주: `GET /api/v1/advertisers/payments`: 내 결제 내역 조회 API 및 UI 구현.
    - 대행사: `POST /api/v1/agencies/portfolios`: 포트폴리오 등록 API 및 UI 구현 (파일 업로드 포함).
    - 대행사: `GET /api/v1/agencies/settlements`: 내 정산 내역 조회 API 및 UI 구현.
    - 광고주: `POST /api/v1/advertisers/reviews`: 대행사 리뷰 작성 API 및 UI 구현.
  - **확인 지점:**
    - 대행사 선정, 결제 내역 조회, 포트폴리오 등록/조회, 정산 내역 조회, 리뷰 작성 등 각 기능이 백엔드와 프론트엔드에서 정상 동작하는가?
  - **커밋 지점:** 결제, 정산, 리뷰, 포트폴리오 기능 구현 (`feat: 핵심 비즈니스 기능(결제,정산,리뷰,포트폴리오) 구현`)

- **5.2 알림 및 채팅 기능 (Backend & Frontend)**
  - [ ] **작업 내용:**
    - 카카오톡 알림 서비스 연동 (광고주/대행사 알림 유형 구현).
    - `GET /api/v1/notifications`: 내 알림 목록 조회 API 및 UI 구현.
    - `PUT /api/v1/notifications/{notification_id}/read`: 알림 읽음 처리 API 및 UI 구현.
    - `ChatRoom`, `ChatMessage` 관련 백엔드 API (목록 조회, 메시지 전송, 파일 첨부) 구현.
    - 선정된 광고주-대행사 간 1:1 채팅방 UI 구현 (메시지 전송, 파일 첨부).
  - **확인 지점:**
    - 알림이 올바른 시점에 발송되고, 사용자에게 표시되는가?
    - 채팅방 생성 및 메시지 전송, 파일 첨부가 정상적으로 동작하는가?
  - **커밋 지점:** 알림 및 채팅 기능 구현 (`feat: 알림 및 채팅 기능 구현`)

---

**6. 관리자 기능 구현 (Admin Feature Implementation)**

- [ ] **작업 내용:** `Admin` 권한 확인 미들웨어 또는 인터셉터 구현.
  - `GET /api/v1/admin/users`: 모든 사용자 조회 및 검색 API 구현.
  - `PUT /api/v1/admin/users/{user_id}/status`: 사용자 상태 변경 API 구현.
  - `DELETE /api/v1/admin/users/{user_id}`: 사용자 삭제 API 구현.
  - 모든 관리자 API 엔드포인트 구현 (입찰, 결제, 정산, 리뷰, 포트폴리오 검수 등).
  - 관리자 대시보드 및 각 관리 기능에 대한 UI 구현.
- **확인 지점:**
  - 관리자 권한으로만 접근 가능한지 확인.
  - 모든 관리 기능이 예상대로 동작하며, 데이터베이스에 올바르게 반영되는가?
- **커밋 지점:** 관리자 기능 백엔드 및 프론트엔드 구현 (`feat: 관리자 기능 백엔드 및 프론트엔드 구현`)

---

**7. 보안 강화 (Security Enhancement)**

- [ ] **작업 내용:**
  - 모든 통신에 HTTPS/SSL/TLS 적용 (배포 환경 설정).
  - 모든 사용자 입력에 대한 강력한 유효성 검증 (`Input Validation`) 로직 강화.
  - 개인정보 (사업자등록번호, 계좌 정보 등) 암호화 저장 및 관리자 접근 로직 재확인.
  - OWASP Top 10 기반 보안 취약점 점검 및 코드 개선.
- **확인 지점:**
  - 개발자 도구에서 통신이 HTTPS로 이루어지는지 확인.
  - SQL Injection, XSS 등 기본적인 공격 시도가 차단되는지 테스트.
  - 민감 정보가 암호화되어 저장되는지 확인.
- **커밋 지점:** 보안 강화 및 취약점 개선 (`refactor: 보안 강화 및 OWASP Top 10 취약점 개선`)

---

**8. 운영 및 배포 준비 (Operations & Deployment Preparation)**

- [ ] **작업 내용:**
  - 로깅(Logging) 시스템 설정 (Spring Boot AOP, Logback 등).
  - 모니터링(Monitoring) 시스템 연동 고려 (APM 툴, 서버 리소스 모니터링).
  - 배포 스크립트 작성 및 문서화.
  - CI/CD 파이프라인 구성 (선택 사항, MVP 이후 고려).
  - 데이터베이스 백업 정책 수립.
- **확인 지점:**
  - 애플리케이션 로그가 의도한 대로 기록되고 관리되는가?
  - 배포 스크립트가 오류 없이 실행되는가?
- **커밋 지점:** 운영 및 배포 준비 (`chore: 운영 및 배포 환경 설정`)

---

**9. 최종 테스트 및 문서화 (Final Testing & Documentation)**

- [ ] **작업 내용:**
  - 전체 시스템 통합 테스트 및 시나리오 기반 테스트 수행.
  - 성능 테스트 (페이지 로딩 시간, API 응답 속도, 동시 사용자 수).
  - API 문서화 (Swagger/OpenAPI).
  - README.md, 설치 가이드, 개발 가이드 등 문서 업데이트.
- **확인 지점:**
  - 모든 기능이 요구사항에 맞춰 정상 작동하는가?
  - API 문서가 최신화되어 있고, 사용하기 쉬운가?
  - 모든 문서가 명확하고 이해하기 쉬운가?
- **커밋 지점:** 최종 테스트 및 문서화 완료 (`docs: 최종 테스트 및 문서화`)
