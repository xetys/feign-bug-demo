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
public class FooUserFeignConfiguration {
    @Bean
    public feign.Logger feignLogger() {
        return new Slf4jLogger(FooUserFeignConfiguration.class);
    }


    @Bean(name = "user-request-interceptor")
    public RequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("user", "user");
    }
}
