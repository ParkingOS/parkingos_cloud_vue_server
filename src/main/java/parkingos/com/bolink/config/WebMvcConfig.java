package parkingos.com.bolink.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import parkingos.com.bolink.interceptor.CorsInterceptor;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Autowired
    CorsInterceptor corsInterceptor;
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        super.configureAsyncSupport(configurer);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(corsInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//         <mvc:resources mapping="/resources/**" location="/resources/**" />
//    <mvc:resources mapping="/images/**" location="/images/**" />
//    <mvc:resources mapping="/1.3/**" location="/1.3/**" />
//        registry.addResourceHandler("/resources/**")//配置访问路径
//                .addResourceLocations("classpath:/images/**")
//                .addResourceLocations("classpath:/1.3/**");//配置文件存放路径，如果多个，通过链式编程逐个添加
    }
}
