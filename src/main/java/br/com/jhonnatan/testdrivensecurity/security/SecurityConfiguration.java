package br.com.jhonnatan.testdrivensecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic(Customizer.withDefaults())
                .authorizeRequests(authorization -> authorization
                        .antMatchers(HttpMethod.GET,"/about").permitAll()
                        .antMatchers(HttpMethod.POST, "/about").hasRole("ADMIN")
                        .antMatchers("/submissions").hasRole("SPEAKER")
                        .anyRequest().authenticated());

        return  httpSecurity.build();
    }
}
