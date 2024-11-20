package com.coggiri.main.configuration;

import com.coggiri.main.configuration.filter.JwtAuthenticationFilter;
import com.coggiri.main.mvc.service.UserService;
import com.coggiri.main.jwtUtils.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    WebSecurityConfig(UserService userService, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception{
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http
                .authorizeHttpRequests((authorizieHttpRequests) ->
                        authorizieHttpRequests
//                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/resources/**", "/static/**", "/css/**").permitAll()
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/swagger-resources/**",
                                        "/v3/api-docs/**"
                                ).hasAnyAuthority("ROLE_SUPER_ADMIN_GROUP_0")
                                .requestMatchers("/api/user/**").permitAll()
                                .requestMatchers("/api/validate/**").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsCustomizer -> corsCustomizer
                        .configurationSource(request -> {
                            var cors = new CorsConfiguration();
                            cors.setAllowedOrigins(List.of("*"));
                            cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                            cors.setAllowedHeaders(List.of("*"));
                            return cors;
                        }))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        }))
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
