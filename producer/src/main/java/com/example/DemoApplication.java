package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class DemoApplication {

	public static class Foo {
		private long id;

		private String value;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public Foo() {
		}

		public Foo(long id, String value) {
			this.id = id;
			this.value = value;
		}
	}


	@EnableWebSecurity
	protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
					.csrf().disable()
					.authorizeRequests()
					.antMatchers("/foos").hasRole("USER")
					.and()
					.httpBasic();
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication()
					.withUser("user")
					.password("user")
					.roles("USER")
					.and()
					.withUser("admin")
					.password("admin")
					.roles("USER", "ADMIN");
		}
	}

	@RestController
	protected static class FooController {
		@RequestMapping("/foos")
		public ResponseEntity<List<Foo>> getFoos(Principal currentUser) {
			Authentication userAuthentication = (Authentication) currentUser;


			if (userAuthentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
				return ResponseEntity.ok(Arrays.asList(new Foo(1L, "admin1"), new Foo(2L, "admin2")));
			} else {
				return ResponseEntity.ok(Arrays.asList(new Foo(3L, "user1"), new Foo(4L, "user2")));
			}
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
