package com.smart.watchboard.controller;

import com.smart.watchboard.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "로그인 API", description = "Oauth2.0 로그인 관련 API(mock)")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtService jwtService;

//    @GetMapping("/users/auth/kakao/callback")
//    @Operation(summary = "카카오 로그인", description = "인가코드를 받아 이후 작업을 진행하고 로그인을 완료한다.")
//    public RedirectView oauthLoginCallback(@RequestParam("code") String code) {
//        RedirectView redirectView = new RedirectView();
//        redirectView.setUrl("https://watchboard.me");
//        redirectView.setStatusCode(HttpStatus.FOUND);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create("/"));
//
//        List<ResponseCookie> cookies = new ArrayList<>();
//        cookies.add(ResponseCookie.from("accessToken", "asdasdqweqwe")
//                .httpOnly(true)
//                .secure(true)
//                .path("/")
//                .build());
//        cookies.add(ResponseCookie.from("refreshToken", "asdklj123")
//                .httpOnly(true)
//                .secure(true)
//                .path("/")
//                .build());
//
//        // 쿠키들을 하나씩 HttpHeaders에 추가
//        for (ResponseCookie cookie : cookies) {
//            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
//        }
//        return redirectView;
//
//        //return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
//
//    }

    @GetMapping("/users/token")
    @Operation(summary = "로그인 후 토큰 전달", description = "로그인 후 request로 받은 리프레시 토큰 검증 후 액세스 토큰과 리프레시 토큰 전달")
    public ResponseEntity<HttpHeaders> getTokens(@CookieValue("refreshToken") String refreshToken) throws UnsupportedEncodingException {
        HttpHeaders headers = jwtService.createHeaderWithTokens(refreshToken);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping("/users/logout")
    @Operation(summary = "로그아웃", description = "로그아웃을 위해 refreshToken 쿠키 만료")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String accessToken) {
        HttpHeaders headers = jwtService.createHeaderWithDeletedCookie(accessToken);
        //headers.setLocation(URI.create("/"));

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

}
