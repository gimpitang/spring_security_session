package com.example.board;

import com.example.board.service.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    암호화 모듈 객체 생성
    @Bean
    public PasswordEncoder passwordEncoder(){
//        암호화 모듈 리턴
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                csrf공격에 대한 설정 일단 disable
                .csrf()
                    .disable()
                .authorizeRequests()
//                특정 리소스에 대해서 권한을 설정(모두 허용)
                .antMatchers("/authors/login" ,"/", "/authors/new")
                .permitAll()
//                아래 내용은 그외 다른 anyrequest는 인증을 요구하는 문구
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                    .loginPage("/authors/login")
//                스프링에서 지원하는 formLogin처리 요청url을 dologin으로 지정(post요청 url설정)
                    .loginProcessingUrl("/doLogin")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler(new LoginSuccessHandler())
                    .and()
                .logout()
//                스프링에서 지원하는 logout기능을 doLogout호출을 통해 처리하겠다는 뜻
                    .logoutUrl("/doLogout")
//                logut성공시 login url로 redirect
                    .logoutSuccessUrl("/login");
    }

}
