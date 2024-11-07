package com.group4.FKitShop.Filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.FKitShop.Request.IntrospectRequest;
import com.group4.FKitShop.Response.AccountsResponse;
import com.group4.FKitShop.Service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@NoArgsConstructor
@Component
public class AuthTokenFilter extends OncePerRequestFilter {


    private final String[] PUBLIC_API = {
            "/fkshop/auth/**",
            "/fkshop/auth/login-google",
            "/fkshop/product/products",
            "/fkshop/product/aproducts",
            "/fkshop/product/latest",
            "/fkshop/product/{id}",
            "/fkshop/product/hot",
            "/fkshop/product/price-asc/{cateID}",
            "/fkshop/product/price-desc/{cateID}",
            "/fkshop/product/by-category/{cateID}",
            "/fkshop/product/by-name/{name}",
            "/fkshop/product/by-id/{id}",
            "/fkshop/product/type/{type}",
            "/fkshop/api/storage/**",
            "/fkshop/categories",
            "/fkshop/categories/{categoryID}",
            "/fkshop/categories/byProductID/{productID}",
            "/fkshop/categories/byTagID/{tagID}",
            "/fkshop/categories/byName/{name}",
            "/fkshop/categories/active",
            "/fkshop/tags",
            "/fkshop/tags/{tagID}",
            "/fkshop/tags/active",
            "/fkshop/tags/byBlogID/{blogID}",
            "/fkshop/tags/byName/{name}",
            "/fkshop/blogs/**",


    };

    @Autowired
    private AuthenticationService authenticationService;



    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {

            String jwt = parseJwt(request);

            if (jwt != null && validateJwtToken(jwt)) {
                // Get account details from the token
                AccountsResponse accountsResponse = authenticationService.tokenAccountResponse(jwt);
                if (accountsResponse != null) {
                    //// Convert role to GrantedAuthority
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + accountsResponse.getRole())); // Role from token

                    log.info(authorities.toString());

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(accountsResponse,
                                    null,
                                    authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info(authentication.toString());
                }
                // Proceed with the next filter
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(new ObjectMapper().writeValueAsString(Map.of(
                        "error", "Unauthorized",
                        "message", "you are my fire",
                        "status", HttpServletResponse.SC_UNAUTHORIZED
                )));
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(new ObjectMapper().writeValueAsString(Map.of(
                    "error", "Forbidden",
                    "message", "You need to login",
                    "status", HttpServletResponse.SC_FORBIDDEN
            )));
        }
    }

    //skip JWT validation for public api
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        log.info("Checking path in shouldNotFilter: {}", path);  // Log the request path
        AntPathMatcher matcher = new AntPathMatcher();
        for (String pattern : PUBLIC_API) {
            if (matcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }


    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);  // Strip "Bearer " prefix and return the token
        }
        return null;  // Return null if Authorization header is invalid or missing
    }

    private boolean validateJwtToken(String token) {
        try {
            //use introspect method to validate token
            return authenticationService.introspect(
                    new IntrospectRequest(token)
            ).isValid();
        } catch (Exception e) {
            log.error("JWT validation error: {}", e.getMessage());
            return false;
        }
    }

}
