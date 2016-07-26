package com.example.excluded;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by on 26.07.16.
 *
 * this @Configuration is inside of an excluded package. We later want to assert, that no beans are autowired in this
 * package
 *
 * @author David Steiman
 */
@Configuration
public class SomeBeanToBeExcluded {

    @Bean(name = "special")
    public Object getSpecialBean() {
        return "some special bean";
    }
}
