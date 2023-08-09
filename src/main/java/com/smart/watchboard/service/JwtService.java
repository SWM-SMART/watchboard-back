package com.smart.watchboard.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String ISSUER_CLAIM = "issuer";
    private static final String ISSUER_CLAIM_VALUE = "wb";
    private static final String BEARER = "Bearer ";

    /**
     * AccessToken 생성 메소드
     */
    public String createAccessToken(Long userId) {
        Date now = new Date();

        return JWT.create() // JWT 토큰을 생성하는 빌더 반환
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) // 토큰 만료 시간 설정
                .withClaim(ISSUER_CLAIM, ISSUER_CLAIM_VALUE)
                .withClaim("userId", userId)
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * RefreshToken 생성
     */
    public String createRefreshToken(Long userId) {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .withClaim(ISSUER_CLAIM, ISSUER_CLAIM_VALUE)
                .withClaim("userId", userId)
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * AccessToken 헤더에 실어서 보내기
     */
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        log.info("재발급된 Access Token : {}", accessToken);
    }

    /**
     * AccessToken + RefreshToken 헤더에 실어서 보내기
     */
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.info("AccessToken: ", accessToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    /**
     * 헤더에서 RefreshToken 추출
     * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
     * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
     */
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * 헤더에서 AccessToken 추출
     * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
     * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public String extractAccessToken(String accessToken) {
        try {
            isAccessTokenFormatValid(accessToken);
        } catch (IllegalArgumentException e) {
            log.info("Error: " + e);
        }

        return accessToken.substring(7);
    }

    public void isAccessTokenFormatValid(String accessToken) {
        if (accessToken == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        if (!accessToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid input format");
        }
    }


    /**
     * userId 추출
     */
    public Optional<Long> extractUserId(String token) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(token)
                    .getClaim("userId")
                    .asLong());
        } catch (Exception e) {
            log.error("토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    /**
     * AccessToken 헤더 설정
     */
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    /**
     * RefreshToken 헤더 설정
     */
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }


    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            log.error("유효하지 않은 토큰입니다. {}", exception.getMessage());
            return false;
        }
    }

    public String extractDecodedRefreshToken(String refreshToken) {
        return refreshToken.substring(7);
    }

    public ResponseCookie setCookieRefreshToken(String refreshToken) throws UnsupportedEncodingException {
        refreshToken = "Bearer " + refreshToken;
        String encodedBearerAndToken = URLEncoder.encode(refreshToken, "UTF-8");
        ResponseCookie cookie = ResponseCookie.from("refreshToken", encodedBearerAndToken)
                .maxAge(60)
                .path("/users/token")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();

        return cookie;
    }

    public HttpHeaders createHeaderWithTokens(String refreshToken) throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        String decodedRefreshToken = extractDecodedRefreshToken(URLDecoder.decode(refreshToken, "UTF-8"));
        log.info(decodedRefreshToken);
        if (isTokenValid(decodedRefreshToken)) {
            headers.add(accessHeader, "Bearer " + createAccessToken(extractUserId(decodedRefreshToken).get()));
            String newRefreshToken = createRefreshToken(extractUserId(decodedRefreshToken).get());
            ResponseCookie cookie = setCookieRefreshToken(newRefreshToken);
            headers.add("Set-Cookie", cookie.toString());
        } else {
            log.info("유효하지 않은 토큰");
            log.info(decodedRefreshToken);
        }

        return headers;
    }
}
