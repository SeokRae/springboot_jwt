## JWT

### Flow 나열 
사용자 추가 -> add(name, pw) -> repository -> DB 등록

사용자 로그인 -> interceptor preHandle
                -> name, pw invalid
                -> DB 확인
                -> 
- [x] 사용자 등록
- 인증 로직
    - [x] 사용자 로그인
        - [x] AccessToken 토큰 발급
        - [ ] Redis 저장
            - [ ] expireAt 설정
            - [ ]  
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
    - [ ] resources 접근 시 토큰 부재 시 -> 로그인 페이지 -> accessToken, refreshToken 재발급
    - [ ] accessToken 구조적 문제 발생 시 -> 로그인 페이지 -> accessToken, refreshToken 재발급 
    - [ ] accessToken expiredException 발생 시 -> refreshToken 확인 후 accessToken 재발급
