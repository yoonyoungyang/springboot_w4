# 🎬 OPENED Backend

> **영화관 특별관 정보 공유 커뮤니티 OPENED의 Backend API Server**

IMAX, 4DX, Dolby Cinema 등 영화관 특별관의 **예매 오픈 정보, 취소표 정보, 좌석 후기 등을 공유하고 실시간 채팅으로 소통할 수 있는 커뮤니티 서비스**의 백엔드 서버입니다.

Spring Boot를 기반으로 REST API를 설계하고, **Spring Security + JWT 인증/인가**, **JPA 기반 데이터 관리**, **WebSocket + STOMP 실시간 채팅**을 구현했습니다.

---

## 🛠 Tech Stack

### Backend

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security
- JWT
- Spring WebSocket
- STOMP
- Gradle

### Database

- H2
- MySQL 전환 예정

### Deployment

- AWS EC2
- Docker
- Docker Compose
- Nginx
- Github Actions

---

## ✨ 주요 기능

### 1. 회원 인증 및 인가

Spring Security와 JWT를 이용해 서버 세션을 사용하지 않는 **Stateless 인증 방식**을 적용했습니다.

- 회원가입 / 로그인
- JWT Access Token 발급
- 로그인 사용자 인증
- 인증이 필요한 API 접근 제어
- 회원 정보 조회 / 수정
- 회원 탈퇴

```text
로그인
  ↓
JWT 발급
  ↓
Authorization: Bearer {token}
  ↓
JwtAuthenticationFilter
  ↓
Token 검증
  ↓
SecurityContext 인증 정보 등록
  ↓
인증 API 접근
```

회원 탈퇴는 데이터를 즉시 삭제하지 않고 삭제 상태를 관리하는 **Soft Delete 방식**으로 구현했습니다.

---

### 2. 특별관 게시판

영화관 특별관과 관련된 정보를 사용자들이 공유할 수 있도록 게시판 REST API를 구현했습니다.

- 게시글 작성
- 게시글 목록 조회
- 게시글 상세 조회
- 게시글 수정
- 게시글 삭제
- 영화 기준 필터링
- 영화관 기준 필터링
- 게시글 조회수 관리

게시글은 다음과 같은 목적의 정보 공유에 활용됩니다.

- 예매 오픈 제보
- 취소표 제보
- 좌석 후기
- 질문

---

### 3. 커뮤니티 상호작용

게시글을 중심으로 사용자 간 상호작용이 가능하도록 관련 API를 구현했습니다.

- 게시글 좋아요
- 댓글 작성 / 수정 / 삭제
- 대댓글 작성
- 게시글 신고
- 사용자 차단

---

### 4. 실시간 채팅

특별관 이용자들이 실시간으로 정보를 공유할 수 있도록 **WebSocket + STOMP 기반 양방향 채팅**을 구현했습니다.

- 채팅방 목록 조회
- 이전 채팅 메시지 조회
- 채팅방별 STOMP 구독
- 메시지 실시간 송수신
- 메시지 DB 저장
- JWT 기반 WebSocket 사용자 인증

```text
Client
  │
  │ STOMP CONNECT + JWT
  ▼
/ws
  │
  ▼
ChannelInterceptor
  │
  │ JWT 검증
  ▼
WebSocket 사용자 인증
  │
  ├── SEND /app/chat
  │
  └── SUBSCRIBE /topic/chat/{roomId}
                         │
                         ▼
                  ChatController
                         │
                         ▼
                    ChatService
                         │
                    메시지 저장
                         │
                         ▼
               SimpMessagingTemplate
                         │
                         ▼
               /topic/chat/{roomId}
```

채팅방 진입 시에는 **REST API를 통해 기존 메시지를 먼저 조회**하고, 이후 새롭게 발생하는 메시지는 **WebSocket을 통해 실시간으로 전달**하도록 역할을 분리했습니다.

---

## 🔐 인증 구조

### REST API 인증

HTTP 요청은 `JwtAuthenticationFilter`에서 JWT를 검증한 후 Spring Security의 `SecurityContext`에 인증 정보를 등록합니다.

이를 기반으로 인증이 필요한 API에서 로그인 사용자를 식별하고 접근 권한을 제어합니다.

### WebSocket 인증

WebSocket은 일반 HTTP API와 인증 흐름이 다르기 때문에 STOMP 연결 단계에서 별도의 JWT 인증을 적용했습니다.

```text
STOMP CONNECT
  ↓
Authorization: Bearer {token}
  ↓
ChannelInterceptor
  ↓
JWT 검증
  ↓
Authentication 객체 생성
  ↓
WebSocket Session 사용자 등록
```

검증된 인증 정보를 WebSocket 세션에 등록하여 클라이언트가 직접 사용자 정보를 신뢰해서 보내는 것이 아니라 **서버가 인증된 사용자를 기준으로 메시지를 처리**하도록 구성했습니다.

---

## 🏗 Backend Structure

```text
src/main/java/kr/adapterz/springboot
├── common
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── security
└── service
```

### 계층별 역할

- `controller` : REST API 및 WebSocket 요청 처리
- `service` : 핵심 비즈니스 로직
- `repository` : JPA 기반 데이터 접근
- `entity` : 데이터베이스 Entity 및 관계 정의
- `dto` : API Request / Response 객체
- `security` : JWT 발급·검증 및 인증 처리
- `config` : Spring Security, WebSocket, CORS 설정
- `exception` : 애플리케이션 예외 처리
- `common` : 공통 응답 및 공통 기능

Controller → Service → Repository로 역할을 분리하고 Entity와 DTO를 분리하여 API 계층과 영속성 계층의 책임을 구분했습니다.

---

## 🔧 주요 구현 포인트

### 1. JWT 기반 Stateless 인증

Spring Security의 세션 인증 대신 JWT를 사용하여 서버가 사용자 세션을 유지하지 않는 구조로 구성했습니다.

REST API 요청마다 전달되는 Access Token을 검증하여 사용자를 인증합니다.

### 2. REST API와 WebSocket 인증 분리

HTTP 요청에서는 Security Filter를 통해 JWT를 검증하고, WebSocket에서는 STOMP `CONNECT` 단계의 `ChannelInterceptor`에서 JWT를 검증합니다.

통신 방식에 맞게 인증 위치를 분리하면서도 이후 비즈니스 로직에서는 동일하게 **인증된 사용자 정보**를 사용할 수 있도록 구성했습니다.

### 3. 이전 메시지와 실시간 메시지 역할 분리

WebSocket만으로 모든 채팅 데이터를 처리하지 않고,

```text
기존 메시지 → REST API
새 메시지   → WebSocket
```

형태로 역할을 분리했습니다.

이를 통해 사용자가 새로고침하거나 다시 채팅방에 접속해도 DB에 저장된 이전 메시지를 복구하고 이후 메시지는 실시간으로 전달받을 수 있습니다.

### 4. 메시지 영속화

WebSocket으로 수신한 채팅 메시지를 단순 전달하는 데서 끝내지 않고 DB에 저장하도록 구성했습니다.

이를 통해 실시간 통신과 채팅 기록 조회 기능을 함께 제공할 수 있도록 했습니다.

### 5. Soft Delete 회원 관리

회원 탈퇴 시 사용자 데이터를 즉시 물리적으로 삭제하지 않고 삭제 시점을 기록하는 방식으로 관리했습니다.

게시글이나 댓글 등 기존 데이터와의 관계를 유지하면서 탈퇴 사용자를 서비스에서 제외할 수 있도록 구성했습니다.

---

## 🚀 Deployment

Backend 서버는 **AWS EC2에서 Docker 컨테이너로 실행**되며 Nginx를 Reverse Proxy로 사용합니다.

```text
Client
  │
  ▼
Nginx
  │
  ├── /api/* ──────→ Spring Boot :8080
  │
  └── /ws ─────────→ Spring WebSocket :8080
```

### 배포 구성

- Spring Boot 애플리케이션 Docker 이미지 생성
- Docker Compose 기반 컨테이너 실행
- Nginx Reverse Proxy 구성
- REST API 요청 `/api/*`를 Backend로 전달
- WebSocket `/ws` 요청을 Backend로 전달
- 운영 환경별 환경 변수 분리

운영 환경에서는 민감한 설정 값을 소스 코드에 직접 작성하지 않고 환경 변수로 주입합니다.

```text
JWT_SECRET_KEY
CORS_ALLOWED_ORIGINS
```

### CI/CD 자동 배포

GitHub Actions를 이용해 `main` 브랜치에 코드가 push되면 EC2 서버에 자동으로 배포되도록 구성했습니다.

main branch push
→ GitHub Actions 실행
→ EC2 SSH 접속
→ Backend / Frontend 최신 코드 pull
→ Docker Compose 재빌드 및 실행

배포에 필요한 EC2 접속 정보와 SSH Private Key는 GitHub Actions Secrets로 관리하여 민감한 정보가 소스 코드에 노출되지 않도록 했습니다.

EC2에서는 최신 코드를 반영한 뒤 `docker compose down`과 `docker compose up -d --build`를 실행해 컨테이너를 다시 빌드하고 실행합니다.

이를 통해 서버에 직접 접속해 수동으로 배포하지 않고, `main` 브랜치에 변경사항이 반영되면 최신 버전이 자동으로 배포되도록 구성했습니다.

---

## 💻 Local Development

저장소를 로컬 환경에서 직접 실행하는 경우 다음과 같이 실행할 수 있습니다.

### Repository Clone

```bash
git clone https://github.com/yoonyoungyang/springboot_w4.git
cd springboot_w4
```

### Spring Boot 실행

```bash
./gradlew bootRun
```

기본 개발 서버는 다음 주소에서 실행됩니다.

```text
http://localhost:8080
```

---

## 🗄 Database

현재 개발 환경에서는 **H2 Database**를 사용합니다.

```text
Spring Data JPA
      │
      ▼
     H2
```

향후 운영 데이터의 영속성과 관리 편의성을 위해 **MySQL 기반 데이터베이스로 전환할 예정**입니다.
