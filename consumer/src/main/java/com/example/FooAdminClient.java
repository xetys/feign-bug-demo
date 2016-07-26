package com.example;

import com.example.excluded.FooAdminFeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by on 26.07.16.
 *
 * @author David Steiman
 */
@FeignClient(value = "prod-svc", configuration = FooAdminFeignConfiguration.class)
public interface FooAdminClient {
    @RequestMapping("/foos")
    List<Foo> getFoos();
}
