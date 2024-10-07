package com.group4.FKitShop.Configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@CrossOrigin(origins = "http://localhost:5173")
public class SecurityConfig {

    private static final String[] POST_PUBLIC_API = {
            "/accounts/signup",
            "/auth/login",
            "/auth/introspect",
    };
    private static final String[] GET_PUBLIC_API = {
            "/product/latest",
            "/product/{id}",
            "/product/aproducts"
    };
//    //secretkey
//    @Value("${jwt.signerKey}")
//    private String signerKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        // Enable CORS and disable CSRF
        httpSecurity.cors().and().csrf(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(request ->
//                request.requestMatchers(HttpMethod.POST, POST_PUBLIC_API).permitAll()
//                        .requestMatchers(HttpMethod.GET, GET_PUBLIC_API).permitAll()
//                        .anyRequest().authenticated()
                        request.anyRequest().permitAll()
        );

//        //register authentication provider supporting jwt token
//        httpSecurity.oauth2ResourceServer(oauth2 ->
//                //jwt decoder: decode jwt truyen vao
//                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder())
//        );
        return httpSecurity.build();
    }
    //jwt decoder interface
//    @Bean
//    JwtDecoder jwtDecoder() {
//        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
//        return NimbusJwtDecoder
//                .withSecretKey(secretKeySpec)
//                .macAlgorithm(MacAlgorithm.HS512)
//                .build();
//
//    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    //CORS handling
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin("http://localhost:5173");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource url = new UrlBasedCorsConfigurationSource();
        url.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(url);
    }


}
