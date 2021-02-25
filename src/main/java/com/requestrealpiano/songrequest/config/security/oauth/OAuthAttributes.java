package com.requestrealpiano.songrequest.config.security.oauth;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String googleOauthId;
    private final String name;
    private final String email;
    private final String avatarUrl;

    @Builder
    private OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String googleOauthId, String name,
                            String email, String avatarUrl) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.googleOauthId = googleOauthId;
        this.name = name;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }

    public static OAuthAttributes of(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                              .googleOauthId((String) attributes.get("sub"))
                              .name((String) attributes.get("name"))
                              .email((String) attributes.get("email"))
                              .avatarUrl((String) attributes.get("picture"))
                              .attributes(attributes)
                              .nameAttributeKey(userNameAttributeName)
                              .build();
    }
}
