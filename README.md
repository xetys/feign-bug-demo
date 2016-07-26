# Spring Cloud Netflix Feign Bug Demo

using `@FeignClient` in spring currently has one fatal issue, which is
demonstrated with this repository.

## how to run
You will need BASH, Java 8 SDK and docker, to run the test script.
During the test, ports 8761 and 8080 have to be free to use.

To run the entire test, just

``` sh
./test.sh
```
This script is:

  * starting an eureka server
  * starting producer service
  * running `WebIntegrationTests` from the consumer service
  * terminating the producer
  * stopping an removing the eureka container

## experiment

We assume some foreign service, serving "foo" entities. You have to be authenticated
to access this the foo resource and have a role "USER". If you have "ADMIN", you
will see different data, as when "USER".

This behavior is implemented using plain spring boot with spring security in
the "producer" service. Additionally it registers to an eureka instance, so some
"consumer" service is able to fetch this.

So the consumer service should fetch data from consumer, automatically passing
basic auth of "users he know". While this may not clearly makes sense for real world,
something very similar is happening consuming OAuth2 secured resource servers with
client credentials grant!
So to get the data correctly, we define 2 clients, with individually set up
basic auth credentials using feign client configuration.

Since the main application
does **not** use `@SpringBootApplication`, but `@EnableAutoConfiguration`,
and an exclude filter on `@ComponentScan`, the two different configuration
do not declare the internal beans.

So the expected result of these 2 clients is: the `UserFooClient` retrieving
at least a foo entity with value "user1", while the `AdminFooClient`s result
should contain a "admin1" foo.

To verify, which result is intended, the same test is done using plain old
`RestTemplate`.
## consequences

This issue leads towards we can't really use multiple feign clients in spring boot
using the `@FeignClient` annotation, because as soon one client is using
some authorization flow (basic, oauth2), this is applied to all other
clients.
