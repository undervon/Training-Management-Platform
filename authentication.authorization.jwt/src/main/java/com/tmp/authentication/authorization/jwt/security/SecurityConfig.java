package com.tmp.authentication.authorization.jwt.security;

import com.tmp.authentication.authorization.jwt.models.enums.RoleValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Log4j2
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenFilter jwtTokenFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> userDetailsService().loadUserByUsername(username));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        http.exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                                authException.getMessage()))
                .and();

        http.exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, authException) -> response.sendError(HttpServletResponse.SC_FORBIDDEN,
                                authException.getMessage()))
                .and();

        http.authorizeRequests()
                .antMatchers("/api/1.0/tmp/auth/login/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/validateAccessToken/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/validateRefreshToken/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/generateAccessToken/**").permitAll()
                // Private endpoints
                .antMatchers("/api/1.0/tmp/auth/addUser/**").hasAuthority(RoleValue.ADMIN.getAuthority())
                .antMatchers("/api/1.0/tmp/auth/deleteUser/**").hasAuthority(RoleValue.ADMIN.getAuthority())
                .antMatchers("/api/1.0/tmp/auth/editRole/**").hasAuthority(RoleValue.ADMIN.getAuthority())
                .antMatchers("/api/1.0/tmp/auth/deleteRole/**").hasAuthority(RoleValue.ADMIN.getAuthority())
                .antMatchers("/api/1.0/tmp/auth/getUser/**").hasAuthority(RoleValue.ADMIN.getAuthority())
                .antMatchers("/api/1.0/tmp/auth/getUsers/**").hasAuthority(RoleValue.ADMIN.getAuthority())
                .anyRequest().authenticated();

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder BCryptPasswordEncoderBean() {
        return new BCryptPasswordEncoder();
    }
}
