package com.example.gestion_achat2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;

    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                        auth -> {
                            auth.requestMatchers("/css/**", "/js/**", "/img/**", "/libs/**", "/res/**", "/login").permitAll();

                            auth.requestMatchers("/rh").hasRole("RH");
                            auth.requestMatchers("/service").hasRole("SERVICE");
                            auth.requestMatchers("/user").hasRole("USER");

                            auth.anyRequest().authenticated();
                        }
                ).formLogin(
                        form -> {
                            form.loginPage("/login");
                            form.defaultSuccessUrl("/", true);
                            form.failureUrl("/login?error");
                            form.usernameParameter("email");
                            form.passwordParameter("password");
                        }
                )
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }
}
