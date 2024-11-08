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
            "/product/products",
            "/product/aproducts",
            "/product/latest",
            "/product/{id}",
            "/product/hot",
            "/product/price-asc/{cateID}",
            "/product/price-desc/{cateID}",
            "/product/by-category/{cateID}",
            "/product/by-name/{name}",
            "/product/by-id/{id}",
            "/product/type/{type}",
            "/api/storage/**",
            "/tags",
            "/tags/{tagID}",
            "/tags/active",
            "/tags/byBlogID/{blogID}",
            "/tags/byName/{name}",
            "/categories",
            "/categories/{categoryID}",
            "/categories/byProductID/{productID}",
            "/categories/byTagID/{tagID}",
            "/categories/byName/{name}",
            "/categories/active",
            "/blogs",
            "/blogs/{blogID}",
            "/blogs/active",
            "/blogs/byTagID/{tagID}",
            "/blogs/dateDesc",
            "/blogs/dateAsc",
//            "/lab-guide/guide/{guideID}",
//            "/lab-guide/guide-by-labID/{guideID}",
            "/lab-guide/all",
            //===================
            "/accounts/register",
            "/accounts/info",
            "/carts/**",
            "/delivery/**",
            "/lab/product/**",
            "/feedback/**",
    };

    private static final String[] ACCOUNT_API = {
            "/accounts/avatar/**",
            "/accounts/password/**",
            "/accounts/password/confirm",
            "/accounts/updateinfo/**",
            //{id}
            "/accounts/**",
            "/accounts/avatar/**",
            "/accounts/info",
            "/accounts/password/**",
            "/orders/details/**",
            "/orders/find/**",
            "orders/checkout",
            "/check-out",
            "/submitOrder",
            "/questions/**",
            "/lab/account/**",
            "/lab/download",
            "/lab/get-link-download",
            "/lab/status-labs/**",
            "/support/create",
            "/support/status",
            "/support/support-accountID/**",
            "/support/supports/**",
            "/wishlists/**",
    };

    private static final String[] ADMIN_API = {
            "/orders/allorders",
            "/admin/update/{id}",
            "/accounts/allAccounts",
            "/accounts/createAccount",
            "/accounts/listAccounts",
            "/accounts/updateAccount/{id}",
    };

    private static final String[] STAFF_API = {
            "/accounts/customer",
            "/orders/**",
    };

    private static final String[] MANAGER_API = {
            "/support/status",
            "/support/support-date",
            "/components/**"
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
                                .requestMatchers(PUBLIC_API).permitAll()

                                .requestMatchers(ACCOUNT_API).hasAnyRole("user", "admin", "staff", "manager")

                                .requestMatchers(ADMIN_API).hasRole("admin")

                                .requestMatchers(MANAGER_API).hasAnyRole("admin", "manager")

                                .requestMatchers(HttpMethod.GET,
                                        "/accounts/customer",
                                        "/product/report/sales",
                                        "lab/labs").hasAnyRole("admin", "manager")

                                .requestMatchers(HttpMethod.PUT,
                                        "/product/{productID}",
                                        "/tags/{tagID}",
                                        "/categories/{categoryID}",
                                        "/blogs/{blogID}",
                                        "/lab-guide/info/{guideID}",
                                        "/lab/{id}").hasAnyRole("admin", "manager")

                                .requestMatchers(HttpMethod.DELETE,
                                        "/product/{productID}",
                                        "/product/images",
                                        "/tags/{tagID}",
                                        "/categories/{categoryID}",
                                        "/blogs/{blogID}",
                                        "/lab-guide/{guideID}",
                                        "/lab/{id}").hasAnyRole("admin", "manager")

                                .requestMatchers(HttpMethod.POST,
                                        "/tags",
                                        "/categories",
                                        "/product/add",
                                        "/product/add-images/{productID}",
                                        "/product/image/{productID}/{imageID}",
                                        "/blogs",
                                        "/lab-guide/create",
                                        "/lab/labs",
                                        "/lab/pdf/create/{labID}",
                                        "/lab/upload-img",
                                        "/lab/upload-lab/{labID}").hasAnyRole("admin", "manager")

                                .requestMatchers(STAFF_API).hasAnyRole("admin", "staff")
                                .anyRequest().authenticated()
//                                .anyRequest().permitAll()
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
