package com.cineplanetfactory.retocp.adapters.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private AuthHandler authHandler;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * @return Codificador de Contraseña por Bcrypt
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/rest/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/users/**").permitAll()
                .antMatchers("/v3/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers(HttpMethod.GET, "/productos/**").permitAll()
                .antMatchers(HttpMethod.POST, "/productos/**").hasAnyAuthority("VENDEDOR","ADMIN")
                .antMatchers(HttpMethod.PUT, "/productos/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/productos/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/pedidos/**").permitAll()
                .antMatchers(HttpMethod.POST, "/pedidos/**").hasAnyAuthority("VENDEDOR","ADMIN")
                .antMatchers(HttpMethod.PUT, "/pedidos/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/pedidos/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(authHandler)
                .and()
                .formLogin().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
