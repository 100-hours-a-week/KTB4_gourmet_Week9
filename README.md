# KTB4_gourmet_Week9

## 과제 개요

이번 주차 과제에서는 기존 커뮤니티 프론트엔드를 **Gourmet Community(개발 학습 커뮤니티)** 콘셉트로 재디자인하고, 단일 게시판 구조를 **자유 / 질문 / 학습 기록 / 프로젝트 모집** 4개 게시판으로 확장하였다.

Spring 백엔드 API 경로와 쿠키 기반 JWT 인증 연동은 유지한 채, HTML/CSS 중심의 UI/UX 개선과 게시판 분기 UX를 구현하였다.

---

## 주요 구현 내용

### 1. 브랜드 디자인 시스템 구축

로고(`gourmet_community_logo.png`)의 색감을 기준으로 공통 디자인 토큰을 정의하였다.

| 항목 | 내용 |
| --- | --- |
| Forest Green | `#2D4F36` |
| Sage | `#768F7A` |
| Cream | `#F3F1E8` |
| Ink Black | `#0A0F0B` |
| Display Font | Cormorant Garamond |
| Body Font | Source Sans 3 |

공통 색상·폰트·배경 atmosphere는 `css/gc-theme.css`로 분리하였고, 게시판 헤더·사이드바 레이아웃은 `css/board-shell.css`와 `js/board-shell.js`로 공통화하였다.

---

### 2. 로그인 / 회원가입 UI 개편

`login.html`, `signup.html`을 Gourmet Community 브랜드 톤으로 전면 개편하였다.

#### 변경 포인트

- 로고를 화면 상단 브랜드 시그널로 배치
- 다크 그린 배경 + 크림 텍스트의 인증 카드 UI
- 입력 포커스, 활성 버튼, 진입 애니메이션 정리

기존 form id와 `login.js`, `signup.js`의 API 호출 로직은 그대로 유지하여 백엔드 연동이 깨지지 않도록 하였다.

```text
유지한 인증 API
- POST /users/login
- POST /users/signup
```

---

### 3. 게시판 4개 구조 확장

기존 단일 게시글 목록 화면을 게시판별 페이지로 분리하였다.

| 게시판 | 목록 페이지 | 작성 페이지 |
| --- | --- | --- |
| 자유 게시판 | `posts.html` | `post-create.html?board=free` |
| 질문 게시판 | `question.html` | `post-create.html?board=question` |
| 학습 기록 | `study.html` | `post-create.html?board=study` |
| 프로젝트 모집 | `project.html` | `project-create.html` |

공통 구성은 다음과 같다.

- 좌측 접이식 사이드바 네비게이션
- 상단 헤더(로고, 검색창, 프로필 메뉴)
- 게시글 카드(배지, 제목, 좋아요/댓글/조회수, 작성자, 날짜, 썸네일)
- 무한 스크롤 목록 (`GET /posts`)

목록 렌더링은 `js/board-list.js`로 공통화하였다.

---

### 4. 게시글 작성 화면 확장

#### 자유 / 질문 / 학습 기록

`post-create.html`은 기존 작성 폼 구조를 유지하고, `board` 쿼리 파라미터로 게시판별 문구만 다르게 표시한다.

```text
예)
/post-create.html?board=question
→ 질문 등록 문구 / placeholder 적용
```

저장 API는 기존과 동일하다.

```http
POST /users/{userId}/posts
```

#### 프로젝트 모집

`project-create.html`을 추가하여 모집 기간(시작일/종료일)을 함께 입력받도록 하였다.

모집 기간은 Spring에 전용 필드가 없어 content 앞부분에 포함해 저장하고, 목록/상세에서 파싱하여 표시한다.

```text
[모집기간] 2026-07-15 ~ 2026-07-31

본문 내용...
```

---

### 5. 게시판 분류 로직 (프론트)

현재 백엔드 `POSTS` 테이블에는 게시판 구분 컬럼이 없다.  
따라서 기존 작성 API를 유지하면서, 프론트에서 게시판 분류를 관리하도록 구현하였다.

핵심 파일은 `js/board-meta.js`이다.

#### 동작 방식

1. 게시글 작성 성공 시 응답의 `postId`를 추출한다.
2. `localStorage`에 `postId → boardType` 매핑을 저장한다.
3. 목록 API(`GET /posts`)로 전체 게시글을 받은 뒤, 현재 게시판에 맞게 필터링한다.
4. 상세 화면 뒤로가기는 해당 게시판 목록으로 복귀한다.

```text
localStorage 키
- gourmetBoardMap: 게시글별 게시판 분류
- gourmetProjectMeta: 프로젝트 모집 기간 메타
- gourmetLastCreated: 방금 작성한 글 식별용
```

백엔드에 `boardType` / `category` 필드가 생기면 API 값을 우선 사용하도록 확장 가능 구조로 두었다.

---

### 6. 상세 / 회원정보 UI 정리

#### 게시글 상세 (`post-detail.html`)

- Gourmet 테마 CSS 적용
- 좋아요 / 댓글 API는 기존 엔드포인트 유지
- 게시판 분류에 따라 상세 eyebrow 및 뒤로가기 경로 연결

```text
유지한 상세 관련 API
- GET /posts/{postId}
- GET/POST /posts/{postId}/likes/users/{userId}
- GET/POST/PATCH/DELETE /posts/{postId}/comments...
```

#### 회원정보 수정 (`edit-profile.html`)

- HTML 구조와 `edit-profile.js`는 유지
- CSS만 현재 테마에 맞춰 정리

---

### 7. 기존 백엔드 연동 유지

프론트엔드 `api.js`의 공통 요청 방식은 기존 Week8 구조를 그대로 사용한다.

```javascript
credentials: "include"
```

- AccessToken / RefreshToken은 HttpOnly Cookie로 전달
- 401 발생 시 RefreshToken으로 재발급 후 원래 요청 재시도
- localStorage에는 화면 표시용 사용자 정보만 저장

```text
localStorage 저장 값
- userId
- email
- nickname
- profileImage
```

---

## 주요 추가 / 수정 파일

### 추가

| 파일 | 역할 |
| --- | --- |
| `css/gc-theme.css` | 공통 디자인 토큰 / atmosphere |
| `css/board-shell.css` | 게시판 공통 셸(헤더, 사이드바) |
| `css/board-question.css` | 질문 게시판 보조 스타일 |
| `css/board-study.css` | 학습 기록 보조 스타일 |
| `css/board-project.css` | 프로젝트 모집 보조 스타일 |
| `js/board-shell.js` | 사이드바 토글 / 검색 안내 |
| `js/board-meta.js` | 게시판 분류 / 프로젝트 메타 관리 |
| `js/board-list.js` | 게시판 공통 목록 렌더링 |
| `question.html` | 질문 게시판 |
| `study.html` | 학습 기록 |
| `project.html` | 프로젝트 모집 |
| `project-create.html` | 프로젝트 모집 작성 |
| `js/project-create.js` | 프로젝트 모집 작성 로직 |

### 개편

| 파일 | 역할 |
| --- | --- |
| `login.html` / `css/login.css` | 로그인 UI |
| `signup.html` / `css/signup.css` | 회원가입 UI |
| `posts.html` | 자유 게시판 목록 |
| `post-create.html` / `js/post-create.js` | 게시글 작성(게시판 분기) |
| `post-detail.html` / `js/post-detail.js` | 상세 UI 및 게시판 복귀 |
| `edit-profile.html` / `css/edit-profile.css` | 회원정보 수정 UI |

---

## 한계 및 개선 포인트

1. 게시판 분류가 DB가 아닌 `localStorage` 기준이라, 브라우저/기기가 바뀌면 분류가 달라질 수 있다.
2. 근본 해결을 위해서는 Spring `POSTS` 테이블에 `BOARD_TYPE` 컬럼을 추가하고 작성/목록 API에 반영하는 작업이 필요하다.
3. 통합 검색, 인기글/주간 랭킹 등은 추후 확장 가능하다.

---

## 최종 결과

이번 주차 과제를 통해 다음 내용을 구현하였다.

- Gourmet Community 브랜드 디자인 시스템 적용
- 로그인 / 회원가입 UI 재디자인
- 좌측 사이드바 기반 게시판 내비게이션 구성
- 자유 / 질문 / 학습 기록 / 프로젝트 모집 4개 게시판 화면 분리
- 게시판별 작성 진입 및 작성 화면 문구 분기
- 프로젝트 모집 기간 입력 UI 추가
- 기존 Spring 게시글 작성·목록·상세·좋아요·댓글 API 연동 유지
- 쿠키 기반 JWT 인증 흐름 유지
- 백엔드 board 필드 부재에 대한 프론트 게시판 분류 임시 구현
- 회원정보 수정 화면 테마 정리
