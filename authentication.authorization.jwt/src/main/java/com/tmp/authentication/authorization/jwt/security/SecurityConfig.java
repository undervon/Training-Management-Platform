package com.tmp.authentication.authorization.jwt.security;

import com.tmp.authentication.authorization.jwt.models.enums.RoleValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
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
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));

        http.authorizeRequests()
                .antMatchers("/api/1.0/tmp/auth/login/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/validateAccessToken/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/validateRefreshToken/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/generateAccessToken/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/changePassword/**").permitAll()
                .antMatchers("/api/1.0/tmp/auth/**").permitAll()
                // Private endpoints
//                .antMatchers("/api/1.0/tmp/auth/addUser/**").hasAuthority(RoleValue.ADMIN.getAuthority())
//                .antMatchers("/api/1.0/tmp/auth/editUser/**").hasAuthority(RoleValue.ADMIN.getAuthority())
//                .antMatchers("/api/1.0/tmp/auth/deleteUser/**").hasAuthority(RoleValue.ADMIN.getAuthority())
//                .antMatchers("/api/1.0/tmp/auth/editRole/**").hasAuthority(RoleValue.ADMIN.getAuthority())
//                .antMatchers("/api/1.0/tmp/auth/deleteRole/**").hasAuthority(RoleValue.ADMIN.getAuthority())
//                .antMatchers("/api/1.0/tmp/auth/getUser/**").hasAnyAuthority(RoleValue.ADMIN.getAuthority(),
//                        RoleValue.MANAGER.getAuthority(), RoleValue.EMPLOYEE.getAuthority())
//                .antMatchers("/api/1.0/tmp/auth/getUsers/**").hasAuthority(RoleValue.ADMIN.getAuthority())
//                .antMatchers("/api/1.0/tmp/auth/user/**").hasAuthority(RoleValue.ADMIN.getAuthority())
//                .antMatchers("/api/1.0/tmp/auth/subordinateUsers/**").hasAuthority(RoleValue.MANAGER.getAuthority())
//                .antMatchers("/api/1.0/tmp/auth/unassignedUsers/**").hasAuthority(RoleValue.MANAGER.getAuthority())
//                .antMatchers("/api/1.0/tmp/auth/assignUser/**").hasAnyAuthority(RoleValue.MANAGER.getAuthority(),
//                        RoleValue.EMPLOYEE.getAuthority())
//                .antMatchers("/api/1.0/tmp/auth/getUserManager/**").hasAnyAuthority(RoleValue.MANAGER.getAuthority(),
//                        RoleValue.EMPLOYEE.getAuthority())
//
//                .antMatchers("/api/1.0/tmp/auth/image/**").hasAnyAuthority(RoleValue.ADMIN.getAuthority(),
//                        RoleValue.MANAGER.getAuthority(), RoleValue.EMPLOYEE.getAuthority())
//
//                .antMatchers("/api/1.0/tmp/auth/logout/**").hasAnyAuthority(RoleValue.ADMIN.getAuthority(),
//                        RoleValue.MANAGER.getAuthority(), RoleValue.EMPLOYEE.getAuthority())
//
//                .antMatchers("/api/1.0/tmp/auth/createCertificate/**").hasAnyAuthority(RoleValue.MANAGER.getAuthority(),
//                        RoleValue.EMPLOYEE.getAuthority())
//                .antMatchers("/api/1.0/tmp/auth/getCertificatesByUserId/**").hasAnyAuthority(
//                        RoleValue.MANAGER.getAuthority(), RoleValue.EMPLOYEE.getAuthority())
//                .antMatchers("/api/1.0/tmp/auth/*").hasAnyAuthority(
//                        RoleValue.MANAGER.getAuthority(), RoleValue.EMPLOYEE.getAuthority())
                // Swagger UI and API Docs
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/api-tmp-auth-docs/**").permitAll()
                .antMatchers("/api-tmp-auth-docs.*").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoderBean() {
        return new BCryptPasswordEncoder();
    }
}
