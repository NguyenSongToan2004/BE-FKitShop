package com.group4.FKitShop.filters;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

@Slf4j
@NoArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {


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
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(accountsResponse, null, null); // Add roles if applicable

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                // Proceed with the next filter
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(new ObjectMapper().writeValueAsString(Map.of(
                        "error", "Unauthorized",
                        "message", "Unauthorized",
                        "status", HttpServletResponse.SC_UNAUTHORIZED
                )));
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(new ObjectMapper().writeValueAsString(Map.of(
                    "error", "Forbidden",
                    "message", "You are not allowed",
                    "status", HttpServletResponse.SC_FORBIDDEN
            )));
        }
    }

    //skip JWT validation for public api
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        String path = request.getRequestURI();
        AntPathMatcher matcher = new AntPathMatcher();
        // Allow wildcard matching for endpoints like /auth/**
        if (matcher.match("/fkshop/auth/**", path) || matcher.match("/fkshop/product/**", path)) {
            return true;
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
            log .error("JWT validation error: {}", e.getMessage());
            return false;
        }
    }

}
