package com.ecommerce.newshop1.config;

import com.ecommerce.newshop1.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomLoginFailureHandler failureHandler;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http
                .authorizeRequests()
                    .antMatchers("/member/findId", "/member/idConfirm", "/member/sendAuth", "/member/authNumCheck",
                            "/member/findId/sendMessage", "/member/findId/authNum", "/member/findId/findIdResult").permitAll()
                    .antMatchers("/kakao/login", "/join", "/login").permitAll()
                    .antMatchers("/mypage/**", "/member/**", "/kakao/**", "/cart/**", "/order/**", "/board/write/**", "/success").authenticated()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/**").permitAll()
                .and()
                    .csrf()
                .and()
                    .exceptionHandling().accessDeniedPage("/")
                .and() // ????????? ??????
                    .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/")
                    .usernameParameter("userId")
                    .successHandler(successHandler())
                    .failureHandler(failureHandler)
                .and() // ???????????? ??????
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .invalidateHttpSession(true)
                    .logoutSuccessUrl("/")
           ;
    }


    @Bean
    public AuthenticationSuccessHandler successHandler(){
        return new CustomLoginSuccessHandler("/");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

}



