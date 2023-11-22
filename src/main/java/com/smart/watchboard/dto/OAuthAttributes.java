package com.smart.watchboard.dto;

import com.smart.watchboard.common.oauth.KakaoOAuth2UserInfo;
import com.smart.watchboard.common.oauth.OAuth2UserInfo;
import com.smart.watchboard.common.enums.Role;
import com.smart.watchboard.common.enums.SocialType;
import com.smart.watchboard.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Getter
@Slf4j
public class OAuthAttributes {
    private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
    private OAuth2UserInfo oauth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        log.info(nameAttributeKey);
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    /**
     * SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환
     * 파라미터 : userNameAttributeName -> OAuth2 로그인 시 키(PK)가 되는 값 / attributes : OAuth 서비스의 유저 정보들
     * 회원의 식별값(id), attributes, nameAttributeKey를 저장 후 build
     */
    public static OAuthAttributes of(SocialType socialType,
                                     String userNameAttributeName, Map<String, Object> attributes) {

        if (socialType == SocialType.KAKAO) {
            log.info("kakao");
        }
        return ofKakao(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {

        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    /**
     * of메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
     * OAuth2UserInfo에서 가져와서 build
     */
    public User toEntity(SocialType socialType, OAuth2UserInfo oauth2UserInfo) {
        if (oauth2UserInfo.getEmail() == null) {
            return User.builder()
                    .socialType(socialType)
                    .socialId(oauth2UserInfo.getId())
                    .email("aaa")
                    .nickname(oauth2UserInfo.getNickname())
                    .role(Role.USER)
                    .build();
        }

        return User.builder()
                .socialType(socialType)
                .socialId(oauth2UserInfo.getId())
                .email(oauth2UserInfo.getEmail())
                .nickname(oauth2UserInfo.getNickname())
                .role(Role.USER)
                .build();
    }

}
