package com.sweetshop.security;

import com.sweetshop.service.UserService;
import com.sweetshop.util.JwtUtil;
import com.sweetshop.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String email = null;
        String jwtToken = null;

        // 1. Extract JWT token from header
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                email = jwtUtil.extractEmail(jwtToken);
            } catch (Exception e) {
                logger.warn("Invalid JWT token: " + e.getMessage());
            }
        } else if (requestTokenHeader != null) {
            logger.warn("JWT token does not start with Bearer: " + requestTokenHeader);
        }

        // 2. Authenticate if email is valid and no existing authentication
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                User user = userService.findByEmail(email);

                if (jwtUtil.validateToken(jwtToken, user)) {
                    List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(user, null, authorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    logger.warn("JWT token validation failed for user: " + email);
                }
            } catch (Exception e) {
                logger.warn("User authentication failed for email: " + email + ". " + e.getMessage());
            }
        }

        // 3. Proceed with filter chain
        filterChain.doFilter(request, response);
    }
}
