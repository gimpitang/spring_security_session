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
//@EnableWebSecurity어노테이션을 붙여줘야 Securiry를 활성화 시킬 수있따.
//depricated된 사항은 spring-data-jpa 레포 참고
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    암호화 모듈 객체 생성
//    Component
//    @component은 setter나 builder 등을 통해서 사용자가 프로퍼티를 변경해서 생성한 인스턴스를 스프링에게 관리하라고 맡기는 것,
//    1)개발자가 직접 컨트롤이 가능한내부 클래스에 사용
//    2)class에서만 사용이 가능
//    Bean
//    @bean는 클래스를 스프링한테 알아서 인스턴스 생성한 후에 등록(bean으로) 하라고 맡기는 것 . -> Spring IoC 컨테이너(또는 DI 컨테이너)에 의해 생성 및 관리
//    1)개발자가 컨트롤이 불가능한 외부 라이브러리 사용시 사용
//    2)매서드에 붙일 수 있다. 클래스 위에는 @Configuration을 붙여줘야 한다.(객체를 생성할 때 싱글톤을 보장하기 위해)

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
