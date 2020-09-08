## JWT

### 테스트 방법 Flow 
1. 사용자 추가 -> add(name, pw) -> repository -> DB 등록

2. 사용자 로그인 -> interceptor preHandler (Authentication)
                -> name, pw invalid (x)
                -> DB 확인
                -> 사용자 확인 시 토큰 발급

3. resources 접근 
    - -> interceptor preHandler (Authorization)
    - -> accessToken 확인   
        - -> accessToken 있으면 정상 자원 접근
    - -> accessToken expiredException 외 예외시   
        - -> 자원 접근 불가 (토큰 값을 분석할 수 없음)
        - -> accessToken expired 예외   
    - -> [redis] accessToken 존재유무 확인 시 존재하는 경우    
    - -> DB의 refreshToken가 있는 경우  
        - -> refreshToken 유효성 확인 정상               
        - -> accessToken 재발급
    - -> DB의 refreshToken가 있는 경우  
        - -> refreshToken 유효성 확인 비정상(expired)   
        - -> 자원 접근 불가
    - -> DB의 refreshToken가 없는 경우  
        - -> 자원 접근 불가
    - -> [redis] accessToken 존재유무 확인 시 존재하지 않는 경우 
        - -> 자원 접근 불가 (refreshToken과 매핑할 키 값을 얻을 수 없음)
    

### 구현 사항
- [x] 사용자 등록
- 인증 로직
    - [x] 사용자 로그인
        - [x] AccessToken 토큰 발급
        - [x] Redis 저장 (DB 로 대체)

- 인가 / 권한
    - [x] 토큰 유효성 검사
        - 토큰 부재시 예외
            - NullPointerException
        - 토큰의 구조적인 예외
            - SignatureException, MalformedJwtException
        - 토큰 유효기간 예외
            - ExpiredJwtException
        - Jwt 관련 상위 예외
            - JwtException
    
    - [x] resources 접근 시 토큰 부재 시 -> 자원 접근 불가
    - [x] accessToken 예외 발생 시 -> 자원 접근 불가
    - [x] accessToken expiredException 발생 시 -> refreshToken 확인 후 accessToken 재발급
