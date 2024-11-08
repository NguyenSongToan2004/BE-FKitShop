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
            "/lab/product/{productID}",
            "/accounts/register",
            "/delivery/**",
    };

    private static final String[] ACCOUNT_API = {
            "/accounts/avatar/{id}",
            "/accounts/password/{id}",
            "/accounts/password/confirm",
            "/accounts/updateAccount/{id}",
            "/accounts/avatar/{id}",
            "/accounts/info",
            "/api/storage/**",
            "/orders/details/{ordersID}",
            "/orders/find/{accountID}",
            "orders/checkout",
            "/check-out",
            "/submitOrder",
            "/lab/account/{accountID}",
            "/lab/download",
            "/lab/get-link-download",
            "/lab/status-labs/{status}",
            "/questions/byAccountID/{accountID}",
            "/support/create",
            "/support/support-accountID/{accountID}",
            "/support/support-status/{status}",
            "/support/supports/{accountID}/{status}",
            "/wishlists/**",
            "/carts/**",
            "/feedback/**",
    };

    private static final String[] ADMIN_API = {
            "/orders/report/{time}",
            "/admin/update/{id}",
            "/accounts/allAccounts",
            "/accounts/createAccount",
            "/accounts/listAccounts",
            "/accounts/admin/update/{id}",
    };

    private static final String[] MANAGER_POST_API = {
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
            "/lab/upload-lab/{labID}"
    };
    private static final String[] MANAGER_GET_API = {
            "/accounts/customer",
            "/orders/allorders",
            "/product/report/sales",
            "lab/labs",
            "/lab-guide/guide/{guideID}",
            "/lab-guide/guide-by-labID/{guideID}",
            "/lab-guide/all",
    };
    private static final String[] MANAGER_PUT_API = {
            "/product/{productID}",
            "/tags/{tagID}",
            "/categories/{categoryID}",
            "/blogs/{blogID}",
            "/lab-guide/info/{guideID}",
            "/lab/{id}",
            "/components/{id}"
    };
    private static final String[] MANAGER_DELETE_API = {
            "/product/{productID}",
            "/product/images",
            "/tags/{tagID}",
            "/categories/{categoryID}",
            "/blogs/{blogID}",
            "/lab/{id}",
            "/lab-guide/{guideID}",
            "/components/{id}"

    };
    private static final String[] STAFF_GET_API = {
            "/accounts/customer",
            "/questions",
            "/questions/{questionID}",
            "/questions/byLabID/{labID}",
            "/questions/byStatus/{status}",
            "/support/all-support",

    };
    private static final String[] STAFF_PUT_API = {
            "/orders/updatestatus/{ordersID}",
            "/support/status",
            "/support/support-date",
            "/support/update-expected",
            "/questions/{questionID}"
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

                                //user
                                .requestMatchers(ACCOUNT_API).hasAnyRole("user", "admin", "staff", "manager")
                                .requestMatchers(HttpMethod.POST, "/questions").hasAnyRole("user", "admin", "staff", "manager")
                                //admin
                                .requestMatchers(ADMIN_API).hasRole("admin")
                                .requestMatchers(HttpMethod.DELETE, "/accounts/{id}").hasRole("admin")

                                //manager
                                .requestMatchers(HttpMethod.POST, MANAGER_POST_API).hasAnyRole("admin", "manager")
                                .requestMatchers(HttpMethod.GET, MANAGER_GET_API).hasAnyRole("admin", "manager")
                                .requestMatchers(HttpMethod.PUT, MANAGER_PUT_API).hasAnyRole("admin", "manager")
                                .requestMatchers(HttpMethod.DELETE, MANAGER_DELETE_API).hasAnyRole("admin", "manager")
                                //staff
                                .requestMatchers(HttpMethod.PUT, STAFF_PUT_API).hasAnyRole("admin", "staff")
                                .requestMatchers(HttpMethod.GET, STAFF_GET_API).hasAnyRole("admin", "staff")
                                .requestMatchers(HttpMethod.DELETE, "/questions/{questionID}").hasAnyRole("admin", "staff")
                                .anyRequest().authenticated()
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
