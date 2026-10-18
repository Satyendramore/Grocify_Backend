package Grocify.GrocifyNear.configuration;

import Grocify.GrocifyNear.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

    private final UserDetailsServiceImpl userDetailsService;
    private final Jwt jwtFilter;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, Jwt jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/webjars/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/user/register",
                                "/api/v1/user/login"
                        ).permitAll()

                                // PUBLIC GET APIs

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/product/**",
                                        "/api/v1/shop/**",
                                        "/api/v1/user/products/compare",
                                        "/api/v1/user/shop/nearby"
                                ).hasRole("USER")


                                .requestMatchers(
                                        "/api/v1/cart/**",
                                        "/api/v1/payment/**",
                                        "/api/v1/order/**"
                                ).hasRole("USER")



                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/v1/product/**",
                                        "/api/v1/shop/**"
                                ).hasRole("SHOP_OWNER")

                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/v1/product/**",
                                        "/api/v1/shop/**"
                                ).hasRole("SHOP_OWNER")

                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/v1/product/**",
                                        "/api/v1/shop/**"
                                ).hasRole("SHOP_OWNER")

                                .anyRequest()
                                .authenticated()

                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
