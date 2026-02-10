package com.rs.app.config;

import com.rs.app.security.OAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final OAuth2UserService oAuth2UserService;

    public SecurityConfig(OAuth2UserService oAuth2UserService) {
        this.oAuth2UserService = oAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/blog-news/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/steam-news/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/games/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/communities/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/files/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/files/**").permitAll()
                .anyRequest().authenticated()
        )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                        .defaultSuccessUrl("/success", true))
                .csrf((csrf) -> csrf.disable())
                .cors(Customizer.withDefaults());
        return http.build();
    }
}
