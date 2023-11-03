package com.example.security.config;

import com.example.security.service.UserDetailsService;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    /**
     * While-listed urls.
     */
    public static final String[] PUBLIC_PATHS = {
            "/health",
            "/api/auth/signup",
            "/v3/api-docs.yaml",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui/index.html"
    };

    /**
     * UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService.
     * UserDetailsService is a core interface that loads user-specific data. It is used throughout the framework as a
     * user DAO and will be used by the DaoAuthenticationProvider during authentication.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * AuthenticationEntryPoint extends BasicAuthenticationEntryPoint
     */
    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;

    /**
     * RSA Public Key
     */
    @Value("${jwt.public.key}")
    RSAPublicKey key;

    /**
     * RSA Private Key
     */
    @Value("${jwt.private.key}")
    RSAPrivateKey privateKey;

    /**
     * DaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider
     * @return authenticationProvider - DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Highest order of SecurityFilterChain defined for Basic Auth authentication only.
     *
     * @param http - HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/auth/token")
                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .csrf(CsrfConfigurer::disable)
                .httpBasic(h -> h.authenticationEntryPoint(this.authenticationEntryPoint))
                .sessionManagement(session ->  session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    /**
     * SecurityFilterChain defined for Bearer Token authentication only.
     *
     * @param http - HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(request -> request.requestMatchers(PUBLIC_PATHS).permitAll()
                        .anyRequest().authenticated())
                .httpBasic(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                )
                // XSS protection
                .headers(headers -> headers.xssProtection(
                        xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
                ).contentSecurityPolicy(
                        cps -> cps.policyDirectives("script-src 'self' .....")
                ));

        return http.build();
    }

    /**
     * Returns JwtDecoder object using org.springframework.security.oauth2.jwt.NimbusJwtDecoder implementation
     * which utilizes RSA public key present in the /resources folder of the project.
     *
     * @return JwtDecoder
     */
    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.key).build();
    }

    /**
     * Returns JwtEncoder object using org.springframework.security.oauth2.jwt.NimbusJwtEncoder implementation .
     * which utilizes RSA private key present in the /resources folder of the project.
     *
     * @return JwtEncoder
     */
    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.key).privateKey(this.privateKey).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    /**
     * Password encoder for storing/fetching user password. This is utilized by DaoAuthenticationProvider bean.
     * BCryptPasswordEncoder implementation has been used here.
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean of org.springframework.security.authentication.AuthenticationManager;
     *
     * @param authenticationConfiguration - AuthenticationConfiguration
     * @return AuthenticationConfiguration
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
