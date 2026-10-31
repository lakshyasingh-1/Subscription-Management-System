
package com.subscriptiq.subscriptiq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            SecurityContextRepository securityContextRepository)
            throws Exception {

        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(
                                CookieCsrfTokenRepository.withHttpOnlyFalse()
                        )
                        .csrfTokenRequestHandler(
                                new CsrfTokenRequestAttributeHandler()
                        )
                        .ignoringRequestMatchers(
                                "/api/auth/register",
                                "/api/auth/login"
                        )
                )

                .securityContext(context -> context
                        .securityContextRepository(securityContextRepository)
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.IF_REQUIRED
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Authentication
                        .requestMatchers("/api/auth/**").permitAll()

                        // Admin APIs
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        // Staff APIs
                        .requestMatchers("/api/staff/**")
                        .hasAnyRole("ADMIN", "STAFF")

                        // Product permissions
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/products",
                                "/api/products/**"
                        ).hasAnyRole("ADMIN", "STAFF")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/products/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/products/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/products/**"
                        ).hasRole("ADMIN")

                        // Product Variant permissions
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/product-variants",
                                "/api/product-variants/**"
                        ).hasAnyRole("ADMIN", "STAFF")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/product-variants/**"
                        ).hasRole("ADMIN")

                        // Customer permissions
                        .requestMatchers(
                                "/api/customers",
                                "/api/customers/**"
                        ).hasAnyRole("ADMIN", "STAFF")

                        // Plan permissions
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/plans",
                                "/api/plans/**"
                        ).hasAnyRole("ADMIN", "STAFF")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/plans/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/plans/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/plans/**"
                        ).hasRole("ADMIN")

                        // Quotation permissions
                        .requestMatchers("/api/quotations/**")
                        .hasAnyRole("ADMIN", "STAFF")

                        // Subscription permissions
                        .requestMatchers("/api/subscriptions/**")
                        .authenticated()

                        // All other APIs require login
                        .anyRequest().authenticated()
                )

                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
