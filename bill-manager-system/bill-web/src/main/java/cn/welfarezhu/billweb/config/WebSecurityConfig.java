package cn.welfarezhu.billweb.config;

import static cn.welfarezhu.billweb.util.SystemConstantValue.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${web.security.admin-name}")
    private String adminName;
    @Value("${web.security.admin-password}")
    private String adminPassword;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(adminName).roles("admin").password(passwordEncoder.encode(adminPassword));
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().mvcMatchers("/resource/**","/static/**","/system/loginFail");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers().frameOptions().disable();

        http.authorizeRequests()
                .antMatchers("/system/admin").hasRole("admin")
                .mvcMatchers("/system/**").authenticated()
                .mvcMatchers("/static/**").permitAll()
                .and()
                .formLogin()
                .loginPage("/system/login")
                .loginProcessingUrl("/system/loginProcess")
                .usernameParameter("adminName")
                .passwordParameter("adminPass")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        HttpSession session=httpServletRequest.getSession();
                        session.setAttribute(IS_MANAGER,true);
                        httpServletResponse.sendRedirect("/system/admin?pageNo="+1);
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        HttpSession session=httpServletRequest.getSession();
                        session.setAttribute(LOGIN_FAIL,true);
                        httpServletResponse.sendRedirect("/system/loginFail");
                    }
                })
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/system/logout")
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        HttpSession session=httpServletRequest.getSession();
                        session.setAttribute(IS_MANAGER,false);
                        httpServletResponse.sendRedirect("/system/login");
                    }
                })
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
