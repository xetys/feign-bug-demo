package com.example.excluded;

import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import feign.slf4j.Slf4jLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by on 26.07.16.
 *
 * @author David Steiman
 */
@Configuration
public class FooAdminFeignConfiguration {
    @Bean
    public feign.Logger feignLogger() {
        return new Slf4jLogger(FooAdminFeignConfiguration.class);
    }

    @Bean(name = "admin-request-interceptor")
    public RequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("admin", "admin");
    }
}
