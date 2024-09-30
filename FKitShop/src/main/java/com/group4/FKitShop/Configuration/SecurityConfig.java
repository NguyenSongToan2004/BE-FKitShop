package com.group4.FKitShop.Configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@EnableWebSecurity
@CrossOrigin(origins = "http://localhost:5173")
public class SecurityConfig {

    private static final String [] POST_PUBLIC_API = {
            "/accounts/signup",
            "/auth/login",
            "/auth/introspect",
    };
    private static final String [] GET_PUBLIC_API = {
            "/product/latest",
            "/product/{id}",
            "/product/aproducts"
    };
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST, POST_PUBLIC_API).permitAll()
                        .requestMatchers(HttpMethod.GET, GET_PUBLIC_API).permitAll()
                        .anyRequest().authenticated()
        );

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }



}
