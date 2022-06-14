package com.tmp.courses.microservice.security;

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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

//        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
//                .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
//                .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));

        http.authorizeRequests()
                .antMatchers("/api/1.0/tmp/auth/login/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/validateAccessToken/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/validateRefreshToken/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/generateAccessToken/**").permitAll()
                // Private endpoints
//                .antMatchers("/api/1.0/tmp/auth/addUser/**").hasAuthority(RoleValue.ADMIN.getAuthority())
//                .antMatchers("/api/1.0/tmp/auth/editUser/**").hasAuthority(RoleValue.ADMIN.getAuthority())
//                .antMatchers("/api/1.0/tmp/auth/deleteUser/**").hasAuthority(RoleValue.ADMIN.getAuthority())
//                .antMatchers("/api/1.0/tmp/auth/editRole/**").hasAuthority(RoleValue.ADMIN.getAuthority())
//                .antMatchers("/api/1.0/tmp/auth/deleteRole/**").hasAuthority(RoleValue.ADMIN.getAuthority())
//                .antMatchers("/api/1.0/tmp/auth/getUser/**").hasAuthority(RoleValue.ADMIN.getAuthority())
//                .antMatchers("/api/1.0/tmp/auth/getUsers/**").hasAuthority(RoleValue.ADMIN.getAuthority())
//                .antMatchers("/api/1.0/tmp/auth/image/**").hasAuthority(RoleValue.ADMIN.getAuthority())
                .antMatchers("/api/1.0/tmp/auth/addUser/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/editUser/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/deleteUser/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/editRole/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/deleteRole/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/getUser/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/getUsers/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/user/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/image/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/logout/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/subordinateUsers/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/unassignedUsers/**").permitAll()
                // Swagger UI and API Docs
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/api-tmp-auth-docs/**").permitAll()
                .antMatchers("/api-tmp-auth-docs.*").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
