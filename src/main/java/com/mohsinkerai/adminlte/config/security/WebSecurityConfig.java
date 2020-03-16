package com.mohsinkerai.adminlte.config.security;

import com.mohsinkerai.adminlte.users.MyUserService;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private DataSource datasource;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
      .antMatchers("/bootstrap/**", "/dist/**", "/plugins/**").permitAll()
      .antMatchers("/users*/**").hasAuthority("ADMIN")
      .anyRequest().authenticated()
      .and()
      .formLogin()
      .failureUrl("/login?error")
      .loginPage("/login")
      .defaultSuccessUrl("/")
      .permitAll()
      .and()
      .logout()
      .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
      .logoutSuccessUrl("/login")
      .permitAll();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Lazy
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth,
    UserDetailsManager userDetailsManager,
    PasswordEncoder passwordEncoder) throws Exception {
    auth.userDetailsService(userDetailsManager).passwordEncoder(passwordEncoder);
//    auth.jdbcAuthentication().dataSource(datasource);

    //TODO: Check whether UserDetailManager By Default Contains AuthenticationManager or We need to Add it
  }

//  @Autowired
//  public void configureUserDetailsManager(MyUserService myUserService, AuthenticationManager authenticationManager) {
//    myUserService.setAuthenticationManager(authenticationManager);
//  }
}
