## 네이버 플레이스 상위노출 입찰 사이트 MVP 개발 체크리스트

1.  **초기 설정 및 환경 구축 (Initial Setup & Environment Configuration)**

    - 모든 항목이 완료되었습니다. ✅

2.  **데이터베이스 설계 및 구현 (Database Design & Implementation)**

    - 모든 항목이 완료되었습니다. ✅

3.  **백엔드 핵심 기능 구현 (Backend Core Feature Implementation)**

    - **3.1 사용자 (User) API**
      - `POST /api/v1/users/register`: 회원가입 (이메일, 소셜) 구현. 완료 ✅
      - `POST /api/v1/users/login`: 로그인 구현. 완료 ✅
      - JWT 기반 인증 및 인가(Authentication & Authorization) 기본 설정. 완료 ✅
      - `GET /api/v1/users/me`: 내 정보 조회 구현. 남았습니다. ⏳
      - `PUT /api/v1/users/me`: 내 정보 수정 구현. 남았습니다. ⏳
      - 비밀번호 해싱 (BCryptPasswordEncoder 등) 적용. 완료 ✅
    - **3.2 광고주 (Advertiser) API**
      - `POST /api/v1/advertisers/bids`: 입찰 요청 등록 API 구현. 완료 ✅
      - 동일한 플레이스 링크 및 키워드 조합에 대한 중복 입찰 요청 불가 로직 구현. 남았습니다. ⏳ (테스트 필요)
      - `GET /api/v1/advertisers/bids`: 내 입찰 요청 목록 조회 API 구현. 완료 ✅
      - `GET /api/v1/advertisers/bids/{bid_id}`: 특정 입찰 요청 상세 조회 API 구현. 완료 ✅
    - **3.3 대행사 (Agency) API**
      - `GET /api/v1/agencies/bids`: 전체 입찰 요청 목록 조회 API 구현 (광고주 정보 블라인드 처리). 완료 ✅
      - `POST /api/v1/agencies/bids/{bid_id}/proposals`: 입찰 제안 제출 API 구현. 완료 ✅
      - 제안 제출 후 1회 수정 가능 로직 구현. 완료 ✅

4.  **프론트엔드 핵심 기능 구현 (Frontend Core Feature Implementation)**

    - **4.1 공통 UI/UX**
      - 메인 페이지, 회원가입/로그인 페이지 구현. 완료 ✅
      - 이메일 및 소셜 로그인(네이버, 카카오) 연동을 위한 UI 구현. UI는 완료되었으나, 실제 연동은 남았습니다. ⏳
      - 추가 회원 정보(사업자등록번호, 상호명 등) 입력 폼 구현. 완료 ✅
    - **4.2 광고주 화면**
      - '입찰 요청 등록' 페이지 UI 구현 및 API 연동. 완료 ✅
      - 내 입찰 목록 및 상세 페이지 UI 구현. 완료 ✅
    - **4.3 대행사 화면**
      - 입찰 요청 목록 페이지 UI 구현. 완료 ✅
      - 특정 입찰에 대한 제안 가격 입력 필드 및 제출/수정 버튼 UI 구현, API 연동. 완료 ✅

5.  **추가 기능 구현 및 연동 (Additional Feature Implementation & Integration)**

    - **5.1 결제, 정산, 리뷰, 포트폴리오 기능 (Backend & Frontend)**
      - 광고주: `PUT /api/v1/advertisers/bids/{bid_id}/select`: 대행사 선정 API 구현 및 UI 연동. 남았습니다. ⏳
      - 광고주: `GET /api/v1/advertisers/payments`: 내 결제 내역 조회 API 및 UI 구현. 남았습니다. ⏳
      - 대행사: `POST /api/v1/agencies/portfolios`: 포트폴리오 등록 API 및 UI 구현 (파일 업로드 포함). 남았습니다. ⏳
      - 대행사: `GET /api/v1/agencies/settlements`: 내 정산 내역 조회 API 및 UI 구현. 남았습니다. ⏳
      - 광고주: `POST /api/v1/advertisers/reviews`: 대행사 리뷰 작성 API 및 UI 구현. 남았습니다. ⏳
    - **5.2 알림 및 채팅 기능 (Backend & Frontend)**
      - 카카오톡 알림 서비스 연동 (광고주/대행사 알림 유형 구현). 남았습니다. ⏳
      - `GET /api/v1/notifications`: 내 알림 목록 조회 API 및 UI 구현. 남았습니다. ⏳
      - `PUT /api/v1/notifications/{notification_id}/read`: 알림 읽음 처리 API 및 UI 구현. 남았습니다. ⏳
      - ChatRoom, ChatMessage 관련 백엔드 API (목록 조회, 메시지 전송, 파일 첨부) 구현. 남았습니다. ⏳
      - 선정된 광고주-대행사 간 1:1 채팅방 UI 구현 (메시지 전송, 파일 첨부). 남았습니다. ⏳

6.  **관리자 기능 구현 (Admin Feature Implementation)**

    - 모든 항목이 남았습니다. ⏳

7.  **보안 강화 (Security Enhancement)**

    - 모든 항목이 남았습니다. ⏳

8.  **운영 및 배포 준비 (Operations & Deployment Preparation)**
    - 모든 항목이 남았습니다. ⏳
