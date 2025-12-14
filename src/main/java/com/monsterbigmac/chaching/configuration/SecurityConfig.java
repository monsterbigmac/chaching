package com.monsterbigmac.chaching.configuration;

import com.monsterbigmac.chaching.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application-${spring.profiles.active}.yml")
public class SecurityConfig {

    @Autowired
    private CustomBasicAuthenticationEntryPoint customBasicAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(req -> req
                .requestMatchers("/api/auth/**")
                .permitAll()
                .requestMatchers("/api/csrf")
                .permitAll()
                .anyRequest()
                .authenticated());

        http.httpBasic((basic) -> basic
                .authenticationEntryPoint(customBasicAuthenticationEntryPoint)
                .addObjectPostProcessor(new ObjectPostProcessor<BasicAuthenticationFilter>() {
                    @Override
                    public <O extends BasicAuthenticationFilter> O postProcess(O filter) {
                        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
                        return filter;
                    }
                }));

        http.cors(Customizer.withDefaults());

        http.csrf(csrf -> csrf
                .csrfTokenRepository(new HttpSessionCsrfTokenRepository()));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new JpaUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    CorsConfig corsConfig;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        System.out.println("aaaaaaaaa" + corsConfig.getCorsAllowedOrigins());
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsConfig.getCorsAllowedOrigins());
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
