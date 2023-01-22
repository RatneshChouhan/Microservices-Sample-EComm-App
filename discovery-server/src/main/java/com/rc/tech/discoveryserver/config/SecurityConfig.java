package com.rc.tech.discoveryserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${eureka.username}")
    private String userName;

    @Value("${eureka.password")
    private String password;

    @Bean
    public InMemoryUserDetailsManager configure () {
    //    UserDetails user = User.withUsername("eureka").password("password").authorities("USER").build();
        return new InMemoryUserDetailsManager(
                User.withUsername(userName)
                    .password("{noop}" + password)
                //    .password(bCryptPasswordEncoder.encode(password))
                    .authorities("USER")
                    .build());
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
        return http.build();
    }

}
