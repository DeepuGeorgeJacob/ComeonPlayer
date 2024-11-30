package se.web.comeon.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/**")
                        /*.requestMatchers(
                                "/h2-console/**",
                                "/api/player/register",
                                "/api/player/login",
                                "/api/player/update",
                                "/api/player/logout/**",
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
                        )*/.permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
                // Disable CSRF for H2 Console (optional, for local development only)
                .csrf(AbstractHttpConfigurer::disable)
                // Allow H2 Console to render frames
                .headers(httpSecurityHeadersConfigure -> httpSecurityHeadersConfigure.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        return http.build();
    }
}
