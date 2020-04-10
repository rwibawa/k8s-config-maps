# optimus-prime
* [Tutorial](https://dzone.com/articles/configuring-java-apps-with-kubernetes-configmaps-a)
* [Source Code](https://github.com/rwibawa/configmaps-transformers)

## 1. Command:
```bash
$ DOCKER_HOST=tcp://localhost:2375 \
./mvnw clean install -Dmaven.test.skip=true -Dmaven.repo.local=./.m2/repository -s ./settings.xml

# Start the docker container
$ docker run -d -p 8099:8080 --name optimus-prime rwibawa/optimus-prime
$ docker logs -f optimus-prime
# or
$ docker run -d -p 8099:8080 -e TRANSFORMER_MODE=robot --name optimus-prime rwibawa/optimus-prime

# Stop the docker container
$ docker stop optimus-prime
$ docker rm optimus-prime

```
Open the browser at [http://localhost:8099](http://localhost:8099).

## 2. Generate images:
We hard-code the name and allegiance of Optimus as these can’t change. We get the mode from a configurable property \
(‘transformer.mode’) using Spring Boot’s @Value annotation and we default the value to ‘disguised’. We also use @Value \
to find out what `disguised` and `robot` modes represent for Optimus - we default these values to `TRUCK` and `ROBOT` \
but we can override with ASCII art from a properties file. We can make the two modes more visual by embedding ASCII art\
instead of ‘TRUCK’ and ‘ROBOT’. To do this we can use images from\
[writesup.org](http://www.writeups.org/optimus-prime-transformers-g1-profile/) and put them through\
[ascii-art-generator](http://www.ascii-art-generator.org/) using the HTML option and embed the resulting HTML DIVs \
in the application.properties file as the values for transformer.disguised and transformer.robot. 

We respond to HTTP requests using GetMapping and output everything we need to see which Transformer this is, what mode \
it is in and what the mode translates to for this Transformer. We wrap it all in an `<h1>` to make sure the text output\
is large enough.

## 3. Build with `docker` command:
```bash
$ cd optimus-prime/
$ docker build -t rwibawa/optimus-prime:1.0 .

$ docker images
$ docker run -p 18080:8080 --name optimus-prime rwibawa/optimus-prime:1.0
$ docker ps -a

```
