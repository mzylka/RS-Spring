package com.rs.app.security;

import com.rs.app.domain.entities.User;
import com.rs.app.services.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;

    public OAuth2UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String discordId = oAuth2User.getAttribute("id");
        String username = oAuth2User.getAttribute("global_name");
        String avatar = oAuth2User.getAttribute("avatar");

        User user = userService.createOrUpdate(discordId, username, avatar);
        Map<String, Object> attrs = new HashMap<>(oAuth2User.getAttributes());
        attrs.put("userId", user.getId());

        if (user.isBanned()){
            throw new OAuth2AuthenticationException("User is banned");
        }

        Set<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toSet());

        return new DefaultOAuth2User(authorities, attrs, "id");
    }
}
