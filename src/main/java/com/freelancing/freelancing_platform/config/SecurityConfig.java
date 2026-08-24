package com.freelancing.freelancing_platform.config;

import com.freelancing.freelancing_platform.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .cors(cors -> {})

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                // =========================
                // PUBLIC AUTHENTICATION
                // =========================

                .requestMatchers(
                    "/api/auth/signup",
                    "/api/auth/login"
                ).permitAll()


                // =========================
                // CLIENT ONLY
                // =========================

                // Create project
                .requestMatchers(HttpMethod.POST,
                    "/api/projects"
                ).hasRole("CLIENT")

                // Edit project
                .requestMatchers(HttpMethod.PUT,
                    "/api/projects/*"
                ).hasRole("CLIENT")

                // Delete project
                .requestMatchers(HttpMethod.DELETE,
                    "/api/projects/*"
                ).hasRole("CLIENT")

                // View project proposals
                .requestMatchers(HttpMethod.GET,
                    "/api/projects/*/proposals"
                ).hasRole("CLIENT")

                // Accept proposal
                .requestMatchers(HttpMethod.PUT,
                    "/api/projects/proposal/*/accept"
                ).hasRole("CLIENT")

                // Reject proposal
                .requestMatchers(HttpMethod.PUT,
                    "/api/projects/proposal/*/reject"
                ).hasRole("CLIENT")

                // Start project
                .requestMatchers(HttpMethod.PUT,
                    "/api/projects/project/*/start"
                ).hasRole("CLIENT")

                // Complete project
                .requestMatchers(HttpMethod.PUT,
                    "/api/projects/project/*/complete"
                ).hasRole("CLIENT")

                // Cancel project
                .requestMatchers(HttpMethod.PUT,
                    "/api/projects/project/*/cancel"
                ).hasRole("CLIENT")


                // =========================
                // FREELANCER ONLY
                // =========================

                // Submit proposal
                .requestMatchers(HttpMethod.POST,
                    "/api/projects/*/proposal"
                ).hasRole("FREELANCER")

                // View my proposals
                .requestMatchers(HttpMethod.GET,
                    "/api/projects/my-proposals"
                ).hasRole("FREELANCER")

                // Withdraw/delete proposal
                .requestMatchers(HttpMethod.DELETE,
                    "/api/projects/proposal/*"
                ).hasRole("FREELANCER")


                // =========================
                // AUTHENTICATED USERS
                // =========================

                // Browse projects
                .requestMatchers(HttpMethod.GET,
                    "/api/projects"
                ).authenticated()

                // View individual project
                .requestMatchers(HttpMethod.GET,
                    "/api/projects/*"
                ).authenticated()

                // Client dashboard
                .requestMatchers(
                    "/api/client/dashboard"
                ).hasRole("CLIENT")

                // Freelancer dashboard
                .requestMatchers(
                    "/api/freelancer/dashboard"
                ).hasRole("FREELANCER")


                // Everything else requires login
                .anyRequest().authenticated()
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}