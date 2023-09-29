package com.Rest.forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.Rest.forum.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/forum/register", "/forum/post/{id}", "/forum/post/{postId}/add-comment",
                                "/forum/post/{postId}/comments","/forum/getUsers",
                                "/error", "/css/**")
                        .permitAll()
                        .anyRequest().authenticated());
        // .formLogin((login) ->
        // login.loginProcessingUrl("/login").defaultSuccessUrl("/forum/post/form",
        // true))
        // .logout((logout) ->
        // logout.logoutUrl("/logout").logoutSuccessUrl("/forum/register"));

        http

                .csrf().disable()

                .httpBasic().disable()

                .formLogin().disable()

                .logout().disable();
        return http.build();
    }

}
