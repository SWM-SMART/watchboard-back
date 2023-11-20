package com.smart.watchboard.handler;

import com.smart.watchboard.common.oauth.CustomOAuth2User;
import com.smart.watchboard.common.enums.Role;
import com.smart.watchboard.domain.User;
import com.smart.watchboard.repository.UserRepository;
import com.smart.watchboard.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${front-url}")
    private String frontUrl;

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            if (oAuth2User.getRole() == Role.USER) {
                String refreshToken = jwtService.createRefreshToken(oAuth2User.getUserId());
                ResponseCookie cookie = jwtService.setCookieRefreshToken(refreshToken);
                response.setHeader("Set-Cookie", cookie.toString());
                response.sendRedirect(frontUrl); // 추후 수정
//                response.sendRedirect("http//localhost:8081");
                User findUser = userRepository.findByEmail(oAuth2User.getEmail())
                        .orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 유저가 없습니다."));
                findUser.authorizeUser();
            } else {
                loginSuccess(response, oAuth2User);
            }
        } catch (Exception e) {
            throw e;
        }

    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        log.info("loginSuccess");
        String accessToken = jwtService.createAccessToken(oAuth2User.getUserId());
        String refreshToken = jwtService.createRefreshToken(oAuth2User.getUserId());
        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    }
}
