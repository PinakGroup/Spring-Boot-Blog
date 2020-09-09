package com.li.blog.config.security;

import com.li.blog.config.handler.AuthenticationFailHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.Filter;

/**
 * @author li
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationFailHandler authenticationFailHandler;
    @Override
    public void configure(WebSecurity webSecurity) {
        //解决静态资源被拦截的问题
        webSecurity.ignoring().antMatchers("/static/**","/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //允许所有用户访问"/"和"/index"
        http.authorizeRequests()
                .antMatchers("/", "/index", "/login/github", "/**.html")
                .permitAll();
        //用户验证相关放行
        http.authorizeRequests()
                .antMatchers("/auth/login-error","/auth/register", "/sendValidationCode", "/auth/forgetPassword", "/auth/resetPassword")
                .permitAll();

        //异常处理
        http.authorizeRequests()
                .antMatchers("/error","/runtimeException","/exception")
                .permitAll();
        //博客浏览
        http.authorizeRequests()
                .regexMatchers("/article/list/\\d+", "/article/\\d+", "/article/hot7/\\d+","/article/hot30/\\d+")
                .permitAll();

        //后台管理员权限
        http.authorizeRequests()
                .antMatchers("/admin/**")
                .hasAuthority("admin");

        //基于表单登录验证
        http.authorizeRequests()
                .and()
                .formLogin()//自定义登录页面
                .loginPage("/auth/login")
                .failureHandler(authenticationFailHandler)
                .permitAll();

        //注销
        http.authorizeRequests()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/index")
                .permitAll();

        //文章分类
        http.authorizeRequests()
                .antMatchers("/category/add")
                .authenticated()
                .antMatchers("/category/**", "/search/")
                .permitAll();

        //其他地址的访问均需验证权限
        http.authorizeRequests()
                .anyRequest()
                .authenticated();


        //开启登录时记住我
        http.authorizeRequests().and().rememberMe();

        //Session管理，只允许用户在一个客户端登录
        http.sessionManagement().maximumSessions(1);
        //解决非thymeleaf的form表单提交被拦截问题
        http.csrf().disable();

        //上传图片 解决editormd的 set 'X-Frame-Options' to 'deny'错误
        http.headers().frameOptions().sameOrigin();

        //第三方登录
        http.authorizeRequests().
                and()
                .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);

        //解决中文乱码问题
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter, CsrfFilter.class);
    }

    /**
     * 认证信息管理
     *
     * @param auth  认证管理构建工具
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService()).passwordEncoder(passwordEncoder());
    }

    /**
     *从数据库中读取用户信息
     */
    @Bean
    public UserDetailsService customUserDetailsService() {
        return new CustomUserDetailServiceImpl();
    }

    /**
     * 设置用户密码的加密方式
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
         return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    /**
     * github账号登录
     */
    private final OAuth2ClientContext oAuth2ClientContext;

    private final PrincipalExtractor principalExtractor;
    @Autowired
    public SecurityConfig(OAuth2ClientContext oAuth2ClientContext, PrincipalExtractor principalExtractor, AuthenticationFailHandler authenticationFailHandler) {
        this.oAuth2ClientContext = oAuth2ClientContext;
        this.principalExtractor = principalExtractor;
        this.authenticationFailHandler = authenticationFailHandler;
    }

    private Filter ssoFilter() {

        OAuth2ClientAuthenticationProcessingFilter githubFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/github");
        OAuth2RestTemplate githubTemplate = new OAuth2RestTemplate(github(), oAuth2ClientContext);
        githubFilter.setRestTemplate(githubTemplate);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(githubResource().getUserInfoUri(), github().getClientId());
        tokenServices.setRestTemplate(githubTemplate);
        tokenServices.setPrincipalExtractor(principalExtractor);
        githubFilter.setTokenServices(tokenServices);

        return githubFilter;

    }

    @Bean
    @ConfigurationProperties("github.client")
    public AuthorizationCodeResourceDetails github() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("github.resource")
    public ResourceServerProperties githubResource() {
        return new ResourceServerProperties();
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(
            OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }


}
