package br.com.elastech.ms_usuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(Customizer.withDefaults())
        );

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/usuario").permitAll()
                .requestMatchers("/internal/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/usuario")
                .hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/usuario/**")
                .authenticated()
                .requestMatchers(HttpMethod.PATCH, "/usuario/**")
                .authenticated()
                .requestMatchers(HttpMethod.DELETE, "/usuario/**")
                .authenticated()
                .anyRequest().authenticated()
        );
        return http.build();
    }
}