package com.hugo.coffe.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    UserDetailsServiceImpl detailService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(detailService);
        authProvider.setPasswordEncoder(getEncoder());
        return authProvider;
    }

    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
                .csrf(AbstractHttpConfigurer::disable)   // Deshabilita CSRF
                .authorizeHttpRequests(auth -> auth //authorizeHttpRequests
                    .requestMatchers("/user/login", "/user/signup", "/user/forgotPassword")
                    .permitAll()  // Permite el acceso a estas rutas sin autenticación
                    .anyRequest().authenticated()  // Requiere autenticación para cualquier otra solicitud
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Configura el uso de sesiones sin estado
                );

        // Agrega el filtro JWT antes del UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    //maneja el tipo de password encoder
    @Bean
    public PasswordEncoder getEncoder(){
        return new BCryptPasswordEncoder();
    }

}
