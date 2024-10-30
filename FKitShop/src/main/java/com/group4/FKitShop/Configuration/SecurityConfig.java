package com.group4.FKitShop.Configuration;


import com.group4.FKitShop.Filters.AuthTokenFilter;
import com.group4.FKitShop.JWT.AuthEntryPointJwt;
import com.group4.FKitShop.Service.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
@CrossOrigin(origins = "http://localhost:5173")
public class SecurityConfig {

    private static final String[] PUBLIC_API = {
            "/auth/**",
            "/product/**",
            "/api/storage/**",
            "/tags/**",
            "/categories/**",
            "/tags/**",
            "/blogs/**"
    };

    private static final String[] ADMIN_API = {
            "/orders/allOrders"
    };

    private static final String[] STAFF_API = {

    };

    private static final String[] MANAGER_API = {
            "/accounts/allAccounts"
    };


    //    //secretkey
    @Value("${jwt.signerKey}")
    private String signerKey;

    @Autowired
    AccountsService accountsService;

    @Autowired
    AuthEntryPointJwt unauthorizedHandler;


    @Bean
    AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(accountsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        // disable CORS and CSRF
        httpSecurity
                .cors().and()
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                //session creation is stateless, so each request is authenticated via JWT
                // and doesn't use an old session with different roles
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(PUBLIC_API).permitAll()
//                        .requestMatchers(MANAGER_API).hasAnyRole("admin","manager")
//                        .requestMatchers(STAFF_API).hasAnyRole("admin","staff")
//                        .requestMatchers(ADMIN_API).hasRole("admin")
//                        .anyRequest().authenticated()
                                .anyRequest().permitAll()
                )
                //register authentication provider supporting jwt token
                //jwt decoder: decode jwt truyen vao, verify token
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(jwtDecoder())
                                //change the scope prefix to ROLE_{}
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();

    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        //customize claim scope to role
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("role");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return converter;
    }

    //jwt decoder interface
    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();

    }

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
