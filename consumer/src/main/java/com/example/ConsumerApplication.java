package com.example;

import feign.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@ComponentScan(excludeFilters =
 @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.example\\.excluded.*")
)
@Configuration
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableFeignClients
public class ConsumerApplication {

	@Bean
	public feign.Logger.Level feignLoggerLevel() {
		return Logger.Level.HEADERS;
	}

	@RestController
	protected class FooController {
		private FooAdminClient fooAdminClient;

		private FooUserClient fooUserClient;

        private Object special;

        @Autowired(required = false)
        @Qualifier("special")
        public void setSpecial(Object special) {
            this.special = special;
        }

        @Autowired
		public FooController(FooAdminClient fooAdminClient, FooUserClient fooUserClient) {
			this.fooAdminClient = fooAdminClient;
			this.fooUserClient = fooUserClient;
        }

		@RequestMapping("/user-foos")
        public ResponseEntity<List<Foo>> getUserFoos() {
            List<Foo> foos = fooUserClient.getFoos();

            return ResponseEntity.ok(foos);
        }

		@RequestMapping("/admin-foos")
        public ResponseEntity<List<Foo>> getAdminFoos() {
            List<Foo> foos = fooAdminClient.getFoos();

            return ResponseEntity.ok(foos);
        }
    }



	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}
}
