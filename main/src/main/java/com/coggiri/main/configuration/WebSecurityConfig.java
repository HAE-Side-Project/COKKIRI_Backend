package com.coggiri.main.configuration;

import com.coggiri.main.mvc.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.io.IOException;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    WebSecurityConfig(UserService userService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception{
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http
                .authorizeHttpRequests((authorizieHttpRequests) ->
                        authorizieHttpRequests
                                .requestMatchers("/**").permitAll()
                                .requestMatchers("/user/**").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configure(http))
                .formLogin((formLogin ->
                        formLogin
                                .usernameParameter("userId")
                                .passwordParameter("password")
                                .loginPage("/loginPage")
                                .failureHandler(new AuthenticationFailureHandler() {
                                    @Override
                                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                        response.setContentType("application/json");
                                        response.getWriter().write("{\"message\":\"로그인 실패\"}");
                                    }
                                })
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/homePage")
                ));
        return http.build();
    }
}
