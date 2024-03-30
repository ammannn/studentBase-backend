package com.university.mcmaster.configurations;

import com.university.mcmaster.utils.SecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.TestingAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        return security
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry ->
                    registry
                            .requestMatchers(getPublicRouts()).permitAll()
                            .requestMatchers(getAdminRouts()).hasRole("admin")
                            .anyRequest().authenticated()
                )
                .sessionManagement(config->config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAt(new SecurityFilter(), UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider())
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        return new TestingAuthenticationProvider();
    }

    private String[] getPublicRouts(){
        return new String[]{
                "/api/register",
                "/api/login/**",
                "/api/version",
                "/api/health",
                "/swagger-ui/**",
                "/v3/api-docs/**"
        };
    }

    private String[] getAdminRouts(){
        return new String[]{
          "/api/admin/**"
        };
    }
}
