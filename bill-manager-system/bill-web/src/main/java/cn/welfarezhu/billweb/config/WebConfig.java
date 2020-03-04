package cn.welfarezhu.billweb.config;

import cn.welfarezhu.billweb.filters.BillInitFilter;
import cn.welfarezhu.billweb.interceptor.BillIdentityInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private BillIdentityInterceptor identityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(identityInterceptor)
                .addPathPatterns("/user/**", "/bill/**", "/msg/**","/unit/**","/component/**")
                .excludePathPatterns("/user/login",
                        "/user/login/valid",
                        "/user/register/submit",
                        "/unit/register",
                        "/unit/register/submit",
                        "/unit/register/checkName",
                        "/user/account/check");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/static/favicon.ico")
                .addResourceLocations("classpath:/static/images/favicon.ico");
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public FilterRegistrationBean InitFilter() {
        FilterRegistrationBean<BillInitFilter> registrationBean = new FilterRegistrationBean<>();
        BillInitFilter filter = new BillInitFilter();
        registrationBean.setFilter(filter);
        List<String> urls = new ArrayList<>();
        urls.add("/*");
        registrationBean.setUrlPatterns(urls);
        return registrationBean;
    }
}
