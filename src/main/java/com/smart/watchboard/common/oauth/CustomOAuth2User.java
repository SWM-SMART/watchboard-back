package com.smart.watchboard.common.oauth;

import com.smart.watchboard.common.enums.Role;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Slf4j
public class CustomOAuth2User extends DefaultOAuth2User {
    private Long userId;
    private String email;
    private Role role;

    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      the authorities granted to the user
     * @param attributes       the attributes about the user
     * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
     *                         {@link #getAttributes()}
     */
    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            Long userId,
                            String email, Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

}
