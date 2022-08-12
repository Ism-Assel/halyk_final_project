package kz.halykacademy.bookstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity()
//@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class SecurityConfig {
    private static final String URL_LOGIN = "/auth/**";
    private static final String URL_ERROR = "/error";
    private static final String URL_AUTHOR = "/authors/**";
    private static final String URL_BOOK = "books/**";
    private static final String URL_GENRE = "genres/**";
    private static final String URL_ORDER = "orders/**";
    private static final String URL_PUBLISHER = "publishers/**";
    private static final String URL_USER = "users/**";

    private static final String ROLE_USER = "USER";
    private static final String ROLE_ADMIN = "ADMIN";


    private final JWTFilter jwtFilter;

    @Autowired
    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // настраивает авторизацию
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(URL_LOGIN, URL_ERROR).not().fullyAuthenticated()
//                .antMatchers(HttpMethod.POST, URL_ORDER).hasRole(ROLE_USER)
//                .antMatchers(HttpMethod.PUT, URL_ORDER).hasRole(ROLE_USER)
//                .antMatchers(HttpMethod.GET, URL_AUTHOR).hasAnyRole(ROLE_USER, ROLE_ADMIN)
                .antMatchers(HttpMethod.POST, URL_AUTHOR, URL_BOOK, URL_GENRE, URL_PUBLISHER, URL_USER).hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.PUT, URL_AUTHOR, URL_BOOK, URL_GENRE, URL_PUBLISHER, URL_USER).hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.DELETE, URL_AUTHOR, URL_BOOK, URL_GENRE, URL_ORDER, URL_PUBLISHER, URL_USER).hasRole(ROLE_ADMIN)
                .antMatchers(HttpMethod.GET, URL_AUTHOR, URL_BOOK, URL_GENRE, URL_ORDER, URL_PUBLISHER, URL_USER).authenticated()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // настраивает аутентификацию
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // генерация пароля
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}