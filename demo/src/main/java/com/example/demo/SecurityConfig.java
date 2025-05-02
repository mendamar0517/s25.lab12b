package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import com.example.demo.UserService;
import com.example.demo.User;
import java.util.Arrays;
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.cors(cors -> cors.configurationSource(request -> {
			CorsConfiguration config = new CorsConfiguration();
			config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
			config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
			config.setAllowedHeaders(Arrays.asList("*"));
            config.setAllowCredentials(true);

			return config;
		}))
    .csrf().disable()
	.authorizeHttpRequests(auth -> auth
    .requestMatchers("/", "/api/users/register", "/api/users/login", "/register", "/register.html","/login", "/login.html", "/index.html", "/users.html", "/users", "/admin.html" , "/admin", "/home.html", "/home").permitAll()
    .anyRequest().authenticated());
return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userService.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole()) 
                .build();
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
