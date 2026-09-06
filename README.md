> 💡 **Personal Contribution (Back-end & User App)**
> - **Authentication & Authorization**: BCrypt 단방향 암호화를 적용한 회원가입/로그인 및 사용자 인증 API 구축[span_4](start_span)[span_4](end_span)
> - **Architecture & Reliability**: Entity-DTO 레이어 격리로 도메인 모델 보호 및 Null Safety 예외 처리 표준화[span_5](start_span)[span_5](end_span)
> - **Integration**: HW - Backend - User App 간 이종 시스템 응답 규격화 및 API 데이터 연동 전담[span_6](start_span)[span_6](end_span)


# ♻️ PETicle Team (PET Bottle + Circle)

**"청소년의 분리배출 습관을 바꾸는 AI 기반 게이미피케이션 솔루션"**

PETicle 팀은 한이음 학점연계형 프로젝트를 통해, 자발적 참여가 부족한 청소년(중·고등학생)들이 즐겁게 환경 보호에 동참할 수 있도록 AI 분리배출 수거기와 게임 서비스를 결합한 통합 시스템을 개발했습니다.

---

## 👥 Our Team & Roles

| 이름       | 역할           | 담당 업무                                                         |
| :--------- | :------------- | :---------------------------------------------------------------- |
| **박수연** | **PM**         | 프로젝트 총괄 및 유니티 게임 콘텐츠 개발                          |
| **김혜인** | **Admin**      | 관리자 앱 프론트엔드/백엔드 개발                                  |
| **박고운(Me)** | **User/Unity** | 유저용 앱 프론트엔드/백엔드 개발, 유니티 게임 콘텐츠 개발 및 연동 |
| **고혜인** | **Unity/HW**   | 유니티 게임 콘텐츠 개발 및 DB 최적화                              |
| **윤정서** | **HW/DB**      | 아두이노 제어 로직 설계 및 하드웨어 연동, DB 설계                 |

---

## 🚀 Projects

저희 팀은 시스템의 확장성과 효율적인 관리를 위해 도메인별로 레포지토리를 분리하여 운영하고 있습니다.

- **[Backend](https://github.com/PETicle0312/Backend)**: Spring Boot 기반의 중앙 API 서버
- **[User Client](https://github.com/PETicle0312/User)**: Expo(React Native) 앱 및 Unity 게임 프로젝트
- **[Admin Client](https://github.com/PETicle0312/Admin)**: 관리자용 수거 현황 모니터링 대시보드
- **[Hardware](https://github.com/PETicle0312/Hardware)**: Jetson Nano 기반 YOLOv8 분석 및 아두이노 제어 모듈

---

## 📁 폴더 구조

PETicle은 하드웨어, 서버, 사용자 앱, 관리자 앱을 독립적으로 개발할 수 있도록 저장소를 분리했습니다.

```text
PETicle0312/
├── Backend/                  # 중앙 API 서버
│   ├── src/main/java/com/example/demo/
│   │   ├── user/            # 회원가입, 로그인, 사용자 정보
│   │   ├── school/          # 학교 검색 및 사용자-학교 연결
│   │   ├── device/          # 수거기 상태와 페트병 투입 이력
│   │   ├── game/            # 게임 세션, 점수, 보상
│   │   ├── admin/           # 관리자 인증 및 수거 현황
│   │   ├── openapi/         # 외부 공개 API
│   │   └── common/          # 공통 응답, 예외 처리, SSE
│   └── src/main/resources/  # DB 및 애플리케이션 설정
├── User/                     # Expo 기반 사용자 앱
│   ├── app/user/            # 회원가입, 로그인, 수거 현황 화면
│   ├── components/          # 공통 UI 컴포넌트
│   └── assets/              # 이미지와 폰트
├── Admin/                    # 관리자용 모니터링 클라이언트
├── Hardware/                 # AI 판별 및 수거기 제어
└── Game/                     # Unity 게임
```

---

## ✨ 핵심 기술 (Core Tech)

- **AI**: YOLOv8 모델을 통한 실시간 투명 페트병 객체 탐지 및 분류
- **IoT**: Jetson Nano와 Arduino를 활용한 자동 수거 및 적재량 감지
- **Gamification**: 수거 보상을 게임 아이템(Credit)으로 연동하여 흥미 유발
- **Mobile**: React Native(Expo)를 활용한 크로스 플랫폼 앱 환경 구축

---

## 📅 Timeline

- **수행 기간**: 2025.03.12 ~ 2025.11.17
- **주관**: 한이음 학점연계형 프로젝트

---

## 🧭 시스템 작동 방식

1. 사용자가 수거기에 페트병을 투입합니다.
2. Jetson Nano의 AI 모델이 투입물을 판별하고 결과를 백엔드에 전달합니다.
3. 백엔드는 판별 결과를 사용자·학교별 수거 이력으로 저장합니다.
4. 사용자 앱은 수거 현황과 학교별 통계를 조회하고, 수거 결과에 따라 지급된 게임 권한을 사용합니다.

```text
페트병 투입 → AI 판별 → 수거 이력 저장 → 앱 조회 및 게임 보상
```

---

## 🚀 로컬 실행 가이드

아래 절차는 중앙 API 서버와 사용자 앱을 함께 실행하는 최소 구성입니다.

### 1. 저장소 클론

```bash
git clone https://github.com/PETicle0312/Backend.git peticle-backend
git clone https://github.com/PETicle0312/User.git peticle-user
```

### 2. 실행 환경 준비

- Java 17
- MariaDB 10.x 이상
- Node.js 20 이상
- npm
- Expo Go 또는 Android/iOS 에뮬레이터

### 3. 데이터베이스 및 환경 변수 설정

MariaDB에서 데이터베이스를 생성합니다.

```sql
CREATE DATABASE peticle CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

백엔드가 데이터베이스 비밀번호를 읽을 수 있도록 환경 변수를 설정합니다.

```bash
export DB_PASSWORD="your-mariadb-password"
```

기본 접속 정보는 `localhost:3306`, 데이터베이스명은 `peticle`, 사용자명은 `root`입니다. 다른 값을 사용하려면 `peticle-backend/src/main/resources/application.yml`을 환경에 맞게 변경합니다.

### 4. 백엔드 실행

```bash
cd peticle-backend
./mvnw spring-boot:run
```

서버가 실행되면 다음 주소에서 API 문서를 확인할 수 있습니다.

- Swagger UI: <http://localhost:8080/swagger-ui/index.html>

### 5. 사용자 앱의 서버 주소 설정

실제 기기에서 Expo 앱을 실행하면 `localhost`가 개발 PC를 가리키지 않습니다. 개발 PC와 모바일 기기를 같은 네트워크에 연결한 뒤 아래 파일의 서버 주소를 개발 PC의 LAN IP로 변경합니다.

- `app/user/login.js`
- `app/user/registration.js`
- `app/user/gamemain.js`

```text
http://<개발-PC-IP>:8080
```

### 6. 프론트엔드 의존성 설치 및 실행

```bash
cd peticle-user
npm ci
npm start
```

Expo 개발 서버가 시작되면 Expo Go로 QR 코드를 스캔하거나 Android/iOS 에뮬레이터를 선택합니다.

### 7. 실행 확인

- 백엔드: `http://localhost:8080/swagger-ui/index.html` 접속
- 프론트엔드: Expo 시작 화면 표시
- 통합 동작: 학교 검색 → 회원가입 → 로그인 → 수거 현황 조회 순서로 API 연결 확인

> 하드웨어 판별과 Unity 게임까지 포함한 전체 흐름은 Jetson Nano, Arduino 및 Unity 실행 환경이 추가로 필요합니다.
