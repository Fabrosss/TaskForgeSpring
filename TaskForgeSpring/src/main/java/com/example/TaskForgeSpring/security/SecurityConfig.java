package com.example.TaskForgeSpring.security;



import com.example.TaskForgeSpring.CorsCustomFilter;
import com.example.TaskForgeSpring.NoPopupBasicAuthenticationEntryPoint;
import com.example.TaskForgeSpring.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpSession;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    MyUserDetailsService myUserDetailsService;
    @Autowired
    HttpSession httpSession;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().antMatchers("/").permitAll().and()
                .authorizeRequests().antMatchers("/console/**").permitAll().and();
        httpSecurity.csrf().disable();
        httpSecurity.cors();
        httpSecurity.headers().frameOptions().disable();
        httpSecurity.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .maximumSessions(1).sessionRegistry(sessionRegistry()).and().sessionFixation();
        httpSecurity.httpBasic()
                .and()
                .authorizeRequests().antMatchers("/user/all").hasAuthority("ADMIN").and()
                .authorizeRequests().antMatchers("/user/logout").hasAnyAuthority("ADMIN", "EDITOR", "USER").and()
                .authorizeRequests().antMatchers("/user/all").hasAnyAuthority("ADMIN", "EDITOR").and()
                .authorizeRequests().antMatchers("/user/new/{name}").hasAuthority("ADMIN").and()
                .authorizeRequests().antMatchers("/user/{id}/{status}").hasAnyAuthority("ADMIN", "EDITOR", "USER").and()
                .authorizeRequests().antMatchers("/task/getTask/{id}").hasAnyAuthority("ADMIN", "EDITOR").and()
                .authorizeRequests().antMatchers("/task/edit").hasAnyAuthority("ADMIN", "EDITOR").and()
                .authorizeRequests().antMatchers("/task/new/{name}").hasAnyAuthority("ADMIN", "EDITOR").and()
                .authorizeRequests().antMatchers("/task/edit/{id}").hasAnyAuthority("ADMIN", "EDITOR").and()
                .authorizeRequests().antMatchers("/task/user/{id}").hasAnyAuthority("ADMIN", "EDITOR", "USER").and()
                .addFilterBefore(new CorsCustomFilter(), BasicAuthenticationFilter.class)
                .formLogin();

        httpSecurity.httpBasic().authenticationEntryPoint(new NoPopupBasicAuthenticationEntryPoint());
    }



    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

}

