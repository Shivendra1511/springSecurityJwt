package com.example.securityPractice.configuration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private MyUserDetailsService myUserDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity https){
       return https.csrf(csrf->csrf.disable())
                .authorizeHttpRequests(req->req.requestMatchers("/save-emp").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth->oauth.jwt(Customizer.withDefaults()))
                .httpBasic(Customizer.withDefaults())
                .build();

    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
    @Bean
    public RSAKey rsaKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair=keyPairGenerator.generateKeyPair();
        return new RSAKey
                .Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }
    @Bean
    public JWKSource jwkSource(RSAKey rsaKey){
        JWKSet jwkSet=new JWKSet(rsaKey);
        return ((jwkSelector, securityContext) -> jwkSelector.select(jwkSet));
    }
    @Bean
    public JwtEncoder jwtEncoder(JWKSource jwkSource){
        return new NimbusJwtEncoder(jwkSource);
    }
    @Bean
    public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) rsaKey.toPublicKey()).build();
    }
}
