package com.asmt.bookmanagementsystem.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**", "/api/health/**").permitAll()
                    .anyRequest().authenticated()
            }
            .httpBasic(withDefaults())
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun users(passwordEncoder: PasswordEncoder): UserDetailsService {
        val user = User.builder()
            .username("user")
            .password(passwordEncoder.encode("password"))
            .roles("USER")
            .build()
        return InMemoryUserDetailsManager(user)
    }
}
