package kz.halykacademy.bookstore.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import kz.halykacademy.bookstore.security.JWTUtil;
import kz.halykacademy.bookstore.services.impl.UserInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final UserInfoServiceImpl userInfoService;

    @Autowired
    public JWTFilter(JWTUtil jwtUtil, UserInfoServiceImpl userInfoService) {
        this.jwtUtil = jwtUtil;
        this.userInfoService = userInfoService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && !authHeader.isEmpty() && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if (jwt.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token in Bearer Header");
            } else {
                try {
                    String login = jwtUtil.validateTokenAndRetrieveClaim(jwt);
                    login = login.replace("\"", "");
                    UserDetails userDetails = userInfoService.loadUserByUsername(login);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            userDetails.getPassword(),
                            userDetails.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }

                } catch (JWTVerificationException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token");
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}