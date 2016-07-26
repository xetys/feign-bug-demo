package com.example;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConsumerApplication.class)
@WebIntegrationTest({"server.port:0"})
public class ConsumerApplicationTests {


	//for old school rest tempaltes
	private LoadBalancerClient loadBalancerClient;
	private RestTemplate userRestTemplate;
	private RestTemplate adminRestTemplate;

	//for feign clients
	private FooUserClient fooUserClient;
	private FooAdminClient fooAdminClient;

	@Autowired
	public void setFooUserClient(FooUserClient fooUserClient) {
		this.fooUserClient = fooUserClient;
	}

	@Autowired
	public void setFooAdminClient(FooAdminClient fooAdminClient) {
		this.fooAdminClient = fooAdminClient;
	}

	@Autowired(required = false)
	public void setLoadBalancerClient(LoadBalancerClient loadBalancerClient) {
		this.loadBalancerClient = loadBalancerClient;
	}

	@Before
	public void setUp() throws Exception {
		userRestTemplate = getRestTemplateWithBasicAuth("user", "user");
		adminRestTemplate = getRestTemplateWithBasicAuth("admin", "admin");
	}

	@Test
	public void restTemplateWorks() throws URISyntaxException {
		assertNotNull(loadBalancerClient);

		ServiceInstance serviceInstance = loadBalancerClient.choose("prod-svc");
		URI serviceUrl = loadBalancerClient.reconstructURI(serviceInstance, new URI("http://prod-svc/foos"));


		List<Foo> userFoos = Arrays.asList(userRestTemplate.getForObject(serviceUrl, Foo[].class));
		List<Foo> adminFoos = Arrays.asList(adminRestTemplate.getForObject(serviceUrl, Foo[].class));

		assertTrue(userFoos.stream().map(Foo::getValue).collect(Collectors.toList()).contains("user1"));
		assertTrue(adminFoos.stream().map(Foo::getValue).collect(Collectors.toList()).contains("admin1"));
	}

	@Test
	public void feignClientWorks() throws Exception {
		List<Foo> userFoos = fooUserClient.getFoos();
		List<Foo> adminFoos = fooAdminClient.getFoos();

		assertTrue(userFoos.stream().map(Foo::getValue).collect(Collectors.toList()).contains("user1"));
		assertTrue(adminFoos.stream().map(Foo::getValue).collect(Collectors.toList()).contains("admin1"));
	}

	private RestTemplate getRestTemplateWithBasicAuth(String username, String password) {
		CredentialsProvider credsProviderUser = new BasicCredentialsProvider();
		credsProviderUser.setCredentials(
				AuthScope.ANY,
				new UsernamePasswordCredentials(username, password));
		CloseableHttpClient credClientUser = HttpClients.custom()
				.setDefaultCredentialsProvider(credsProviderUser)
				.build();


		HttpComponentsClientHttpRequestFactory requestFactoryUser = new HttpComponentsClientHttpRequestFactory();
		requestFactoryUser.setHttpClient(credClientUser);


		return new RestTemplate(requestFactoryUser);
	}
}
