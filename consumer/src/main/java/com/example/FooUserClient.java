package com.example;

import com.example.excluded.FooUserFeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by on 26.07.16.
 *
 * @author David Steiman
 */
@FeignClient(value = "prod-svc", configuration = FooUserFeignConfiguration.class)
public interface FooUserClient {
    @RequestMapping("/foos")
    List<Foo> getFoos();
}
