#!/bin/bash

echo "Welcome to my spring feign failure test script."
echo "to make things maximum eazy, I will start everyting in the right order."
echo ""
echo "1. We need a spring cloud eureka server...let's pull a jhipster-registry!"
docker run -d -p 8761:8761 --name demo-registry jhipster/jhipster-registry &
sleep 1
echo ""
echo "ok, this is done, but it need some seconds to boot up. I will now wait until it's up..."
printf "waiting"
curl --silent "admin:admin@localhost:8761" > /dev/null
while [ $? != 0 ];
do
  printf "."
  sleep 5
  curl --silent "admin:admin@localhost:8761" > /dev/null
done;
echo ""
echo "eureka up!"

echo "2. now lets start the producer service"
cd producer
./gradlew bootRun > /dev/null &

echo "ok, this is done, but it need some seconds to boot up. I will now wait until it's up..."
printf "waiting"
curl --silent "admin:admin@localhost:8080" > /dev/null
while [ $? != 0 ];
do
  printf "."
  sleep 5
  curl --silent "admin:admin@localhost:8080" > /dev/null
done;
echo ""
echo "consumer up!"


echo "3. now its time to fire the tests from consumer service!"
sleep 1
cd ../consumer
./gradlew test

echo ""
echo ""

echo "well, you should see 1/2 tests failing. This because I use two feign"
echo "clients with individually set up credentials."
echo "The first test is manually applying basic auth onto a RestTemplate...and"
echo "this works. The second test is checking the feign clients, configured for"
echo "the same scenario, but this fails!"
echo ""
echo "now I will be so nice to clean up for you :)"
ps aux | grep gradlew | awk '{print $2}' | xargs kill -KILL
docker stop demo-registry
docker rm demo-registry
