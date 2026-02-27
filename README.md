# K-Play

KOPIS(공연예술통합전산망) Open API를 활용한 국내 공연 정보 Android 앱입니다.
공연 랭킹, 장르별/지역별 공연 목록, 축제, 수상작, 즐겨찾기, 내 주변 공연 등 다양한 기능을 제공합니다.

---

## 모듈 구조

```
codehong-app-k-play/
├── app/                         # UI 레이어 (Activities, Composables, ViewModels, DI)
├── domain/                      # 도메인 레이어 (UseCases, Repository 인터페이스, 모델, 타입)
├── data/                        # 데이터 레이어 (API, Room DB, Repository 구현체, Mapper, DTO)
└── codehong-submodule-build-logic/  # Gradle Convention Plugin
```

### app 모듈

UI 및 DI 설정을 담당합니다.

| 패키지 | 설명 |
|--------|------|
| `ui/lounge` | 메인 화면 (하단 탭 네비게이션) |
| `ui/performance/detail` | 공연 상세 화면 |
| `ui/localtab` | 지역별 공연 목록 화면 |
| `ui/genre` | 장르별 랭킹 화면 |
| `ui/common` | 공통 Composable 컴포넌트 |
| `ui/theme` | 앱 테마 (색상, 타이포그래피) |
| `di` | Hilt DI 모듈 (ApiModule, RepositoryModule, UseCaseModule, DatabaseModule, DataSourceModule) |
| `manager` | ActivityManager, BitmapManager |

### domain 모듈

비즈니스 로직과 추상화 계층을 담당합니다.

| 패키지 | 설명 |
|--------|------|
| `usecase` | PerformanceUseCase, FavoriteUseCase, PlaceUseCase |
| `repository` | PerformanceRepository, FavoriteRepository, PlaceDetailCacheRepository (인터페이스) |
| `model` | BoxOfficeItem, PerformanceInfoItem, PerformanceDetail, FavoritePerformance, PlaceDetail 등 |
| `type` | GenreCode, RegionCode, BottomTabType, ThemeType, RankTab |
| `Consts` | SharedPreferences 키 상수 |

### data 모듈

원격/로컬 데이터 소스 및 Repository 구현체를 담당합니다.

| 패키지 | 설명 |
|--------|------|
| `remote` | KopisApiService (Retrofit) |
| `datasource` | PerformanceRemoteDataSource, PerformanceLocalDataSource, PrefManager |
| `repository` | PerformanceRepositoryImpl, FavoriteRepositoryImpl, PlaceDetailCacheRepositoryImpl |
| `room` | KPlayDatabase, FavoritePerformanceDao, PlaceDetailDao |
| `model` | API 응답 DTO |
| `mapper` | DTO → Domain 모델 변환 |

---

## 아키텍처

**Clean Architecture + MVI** 패턴을 사용합니다.

```
View (Composable)
  │  ViewEvent
  ▼
ViewModel (BaseViewModel)
  │  ViewState / ViewSideEffect
  ▼
UseCase
  │
  ▼
Repository (interface)
  │
  ▼
RepositoryImpl ──▶ RemoteDataSource (Retrofit)
              └──▶ LocalDataSource  (Room / SharedPreferences)
```

- **ViewEvent**: 사용자 액션 (클릭, 탭 선택 등)
- **ViewState**: UI에 렌더링되는 상태 데이터
- **ViewSideEffect**: 화면 이동, Toast 등 일회성 사이드 이펙트

---

## 기술 스택

| 분류 | 라이브러리 |
|------|-----------|
| 언어 | Kotlin |
| UI | Jetpack Compose, Material3 |
| 아키텍처 | MVI (codehong architecture) |
| DI | Dagger Hilt |
| 비동기 | Coroutines, Flow |
| 네트워크 | Retrofit (codehong network) |
| 로컬 DB | Room |
| 이미지 | Coil |
| 지도 | Naver Maps SDK |
| 미디어 | ExoPlayer (Media3, HLS) |
| 위치 | Google Play Services Location |
| 빌드 | Gradle Convention Plugins |

### 내부 공통 라이브러리 (com.codehong.library)

| 라이브러리 | 역할 |
|-----------|------|
| `widget` | 공통 UI 컴포넌트 |
| `network` | Retrofit 래퍼 (`CallStatus`: Loading / Success / Error) |
| `architecture` | MVI 기반 클래스 (`BaseViewModel`, `ViewEvent`, `ViewState`, `ViewSideEffect`) |
| `util` | 유틸리티 함수 |
| `debugtool` | 디버그 도구 |

---

## 주요 기능

### 홈 탭
- **월간 박스오피스 랭킹**: 전체 / TOP 1~10 / TOP 11~20 / TOP 21~30
- **장르별 랭킹**: 연극, 뮤지컬, 클래식, 국악, 대중음악, 무용, 대중무용, 서커스/마술, 아동, 오픈런
- **지역별 공연**: 전국 17개 시도별 공연 목록
- **축제 목록**: 지역별 축제 정보
- **수상작 목록**: 지역별 수상 공연

### 내주변 탭
- GPS 기반 현재 위치 공연 조회
- 네이버 지도 연동

### 찜 탭
- Room DB를 활용한 즐겨찾기 저장/삭제
- 즐겨찾기 공연 목록 조회

### 설정 탭
- 다크모드 / 라이트모드 / 시스템 테마 전환
- 공연장 정보 캐시 삭제

### 공연 상세
- 기본 정보 (장르, 기간, 장소, 상태)
- 출연진, 가격, 공연시간표, 공연장 안내, 공지사항
- 예매 사이트 바로가기

---

## 지원 장르

| 코드 | 장르 |
|------|------|
| AAAA | 연극 |
| GGGA | 뮤지컬 |
| CCCA | 클래식 |
| CCCC | 국악 |
| CCCD | 대중음악 |
| BBBC | 무용 |
| BBBR | 대중무용 |
| EEEB | 서커스/마술 |
| KID | 아동 |
| OPEN | 오픈런 |

---

## 지원 지역

서울, 부산, 대구, 인천, 광주, 대전, 울산, 세종, 경기, 강원, 충북, 충남, 전북, 전남, 경북, 경남, 제주 (17개 시도 + 시군구 세부 코드)

---

## 권한

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
```

---

## 빌드 설정

프로젝트 루트에 `secrets.properties` 및 `github.properties` 파일이 필요합니다.

```properties
# secrets.properties
KOPIS_API_KEY=...
NAVER_CLIENT_ID=...

# github.properties
url=...
username=...
token=...
```
