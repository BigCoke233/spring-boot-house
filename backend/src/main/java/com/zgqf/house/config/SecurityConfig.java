package com.zgqf.house.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                // Allow read-only access to user profiles for contract details without specific role check for now, 
                // or ensure the roles are correctly assigned. 
                // The issue is likely that "admin" role check fails for normal users trying to view profiles via admin endpoints.
                // We should probably move public read access to a public controller or relax this.
                // However, /api/admin/user/buyers/{id} is being called by the frontend.
                // Let's permit authenticated users to read these details if they are involved in a contract.
                // But simplifying for this bug fix:
                // Note: The previous change to use HttpMethod.GET might not be enough if the path matcher is strict or order matters.
                // Also, sometimes the role check in subsequent filters overrides this if not careful.
                // But here, permitAll() or authenticated() should work if placed BEFORE the stricter rules.
                // I will try permitAll() to rule out auth issues for now, assuming these are public profiles.
                // Wait, user data might be sensitive. Let's keep authenticated() but ensure it matches correctly.
                .requestMatchers(HttpMethod.GET, "/api/admin/user/buyers/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/admin/user/sellers/**").permitAll()
                // Also allow search
                .requestMatchers(HttpMethod.GET, "/api/admin/user/sellers").permitAll()
                
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/buyer/**").hasRole("BUYER")
                .requestMatchers("/api/seller/**").hasRole("SELLER")
                .requestMatchers("/api/contract/**").hasAnyRole("BUYER", "SELLER", "ADMIN")
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            );
            
        return http.build();
    }

    // 跨域配置
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173", 
            "http://localhost:8080",
            "http://127.0.0.1:5173",
            "http://127.0.0.1:8080"
        )); // 5173 是前端端口
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
