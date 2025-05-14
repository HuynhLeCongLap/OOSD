package com.truong.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.truong.springboot.service.CustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((auth) ->
                        auth
                                .requestMatchers("/logon", "/register", "/assets/**").permitAll() // Cho phép truy cập không cần xác thực
                                .requestMatchers("/posts/**").permitAll() // Cho phép tất cả mọi người truy cập trang posts
                                .requestMatchers("/admin/**").hasAuthority("ADMIN") // Chỉ cho phép ADMIN truy cập /admin/**
                                .requestMatchers("/posts/**", "/admin/**").hasAnyAuthority("USER", "ADMIN") // Cho phép USER và ADMIN truy cập các trang liên quan đến posts và admin
                                .anyRequest().authenticated() // Các request khác cần xác thực
                )
                .formLogin(form -> form
                        .loginPage("/logon")
                        .loginProcessingUrl("/logon")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/logon?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/logon?logout")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/assets/**");
    }
}
