package com.tmp.courses.microservice.security;

import com.tmp.courses.microservice.models.enums.RoleValue;
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
                // course-controller
                .antMatchers("/api/1.0/tmp/courses/addCourse/**").hasAnyAuthority(
                        RoleValue.MANAGER.getAuthority(), RoleValue.EMPLOYEE.getAuthority())
                .antMatchers("/api/1.0/tmp/courses/deleteCourse/**").hasAuthority(RoleValue.MANAGER.getAuthority())
                .antMatchers("/api/1.0/tmp/courses/getCourse/**").hasAnyAuthority(
                        RoleValue.MANAGER.getAuthority(), RoleValue.EMPLOYEE.getAuthority())
                .antMatchers("/api/1.0/tmp/courses/getCourses/**").hasAnyAuthority(
                        RoleValue.MANAGER.getAuthority(), RoleValue.EMPLOYEE.getAuthority())
                .antMatchers("/api/1.0/tmp/courses/updateCourse/**").hasAuthority(RoleValue.MANAGER.getAuthority())
                // survey-controller
                .antMatchers("/api/1.0/tmp/courses/addSurvey/**").hasAnyAuthority(
                        RoleValue.MANAGER.getAuthority(), RoleValue.EMPLOYEE.getAuthority())
                // allowed for all for /api/1.0/tmp/courses/{directory} and /api/1.0/tmp/courses/{directory}/{filename}
                .antMatchers("/api/1.0/tmp/courses/**").permitAll()
                // Swagger UI and API Docs
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/api-tmp-courses-docs/**").permitAll()
                .antMatchers("/api-tmp-courses-docs.*").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
