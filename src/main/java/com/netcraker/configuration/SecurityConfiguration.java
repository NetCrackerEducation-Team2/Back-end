package com.netcraker.configuration;


import com.netcraker.security.filter.JwtAuthenticationFilter;
import com.netcraker.security.filter.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfiguration(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/announcements/**", "/auth/**", "api/profile/**", "**").permitAll()
//                .antMatchers(HttpMethod.POST, "api/achievement").hasAnyRole("ADMIN","SUPER ADMIN")
//                .antMatchers(HttpMethod.DELETE, "api/achievement").hasAnyRole("ADMIN","SUPER ADMIN")
//                .antMatchers(HttpMethod.PUT, "api/achievement").hasAnyRole("ADMIN","SUPER ADMIN")
                // for test, in production uncomment upper comments
//                .antMatchers(HttpMethod.DELETE, "/api/announcements/**").permitAll()
                .antMatchers("api/achievement").permitAll()

                .antMatchers(HttpMethod.GET, "/api/books", "/api/announcements/**", "/auth/**", "api/profile/**", "**").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/**", "/api/announcements/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/announcements/**", "/api/announcements/**", "/api/book-overviews/**,", "/api/book-review/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/auth/**", "/api/announcements/**", "**").permitAll()
                .antMatchers("/books", "/book/download", "/announcements", "/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), passwordEncoder()))
                .addFilter(new JwtAuthorizationFilter((authenticationManager())))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        // Code below is only for test purpose
        // In production change to return an instance of BCryptPasswordEncoder
        return NoOpPasswordEncoder.getInstance();
//       return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
