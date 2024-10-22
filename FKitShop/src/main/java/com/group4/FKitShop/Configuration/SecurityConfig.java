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
@CrossOrigin(origins = "http://localhost:5173")
public class SecurityConfig {

    private static final String[] POST_PUBLIC_API = {
            "/accounts/signup",
            "/auth/login",
            "/auth/introspect",
            "/carts/create"
    };
    private static final String[] GET_PUBLIC_API = {
            "/product/latest",
            "/product/{id}",
            "/product/aproducts",
            "/carts/view/{accountID}"
    };
//    //secretkey
    @Value("${jwt.signerKey}")
    private String signerKey;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    AccountsService accountsService;


    @Bean
    public AuthTokenFilter authTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(accountsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

  //       Enable CORS and disable CSRF
//        httpSecurity
//                .cors();
//                .and()
//                .csrf(AbstractHttpConfigurer::disable);
//        httpSecurity
//               .csrf().disable();

//        httpSecurity
//                .csrf().disable() // Có thể tắt CSRF cho mục đích thử nghiệm
//                .authorizeRequests()
//                .antMatchers("/api/auth/login").permitAll() // Cho phép truy cập vào endpoint này
//                .anyRequest().authenticated();

//        httpSecurity.authorizeHttpRequests(request ->
//                request.requestMatchers(HttpMethod.POST, POST_PUBLIC_API).permitAll()
////                        .requestMatchers(HttpMethod.GET, GET_PUBLIC_API).permitAll()
////                        .requestMatchers(HttpMethod.GET,"/accounts" ).hasAnyAuthority("SCOPE_")
////                        .anyRequest().authenticated()
//                        .anyRequest().permitAll()
//        );
//
//
////        //register authentication provider supporting jwt token
//        httpSecurity.oauth2ResourceServer(oauth2 ->
//                //jwt decoder: decode jwt truyen vao
//                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
//        );

        httpSecurity
                .cors().and()
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, POST_PUBLIC_API).permitAll()
                        .requestMatchers(HttpMethod.GET, GET_PUBLIC_API).permitAll()
                        .requestMatchers("/**/admin/**").hasRole("admin")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
//    @Bean
//    JwtAuthenticationConverter converter(){
//        JwtGrantedAuthoritiesConverter j
//
//        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//        return converter;
//    }

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
