# Theatre-Service

## Assumptions
The below features required for any standard microservice has not been considered:
- Spring Security
- Access control
- Distributed Tracing
- Circuit Breaker
- Extensive Logging
- Caching

### Spring Security
Is required for authentication and authorization. There are 2 types of endpoints
- Non-Authenticated
   - GET calls to fetch theatre information
   - GET calls to fetch screen information and which movie is playing
- Authenticated
   - POST to create new theatre/screen information
   - PUT calls to update the theatre/screen information

### Access control
Since our application is multi-tenant, i.e. theatre owner can manage their own theatre information,
we need to have access control in place which will ensure the below
- Theatre owners can only update their own theatre related information
- Theatre owners can only add screen/shows to their own theatre

### Distributed Tracing
Any standard microservice needs to have support for distributed tracing.
A single operation can consist of multiple operations. In such cases, it is imperative that we have a unified view of
a request flow through our application. This is provided through Distributed Tracing.

### Circuit Breaker
Usage of circuit breaker pattern is strongly adviced when calling other microserices. Use the resilience4j library
to implement a timeout and exponential fallback strategy coupled a fallback.



### Detailed Logging
Currently only basic logging has been implemented as a proof-of-concept. In the final project we need to have
little more detailed logging.

### Caching
The application should have support for the below caching functions
- Hibernate L2 cache
- External distributed cache (ElasticSeearch either redis or memcached)

# Design Patterns

The below design patterns are expected to be implemented
- Multi Layered / multi tiered
- Strategy pattern for cases like rate decision where multiple options exists and one has to be chosen
- Bulkhead in cases where we have multiple service calls to other services, it is recommended to use  a separate thread pool per service to have a bulkhead implemented.
- CQRS maybe utilized for cases where read is extensive and write are far and few in between (e.g. marking a show as soldout)

# Testing
For all implementation the below are mandatory
- Having unit tests for custom implementation within Repository
- Having unit tests for Service layer
- Having unit tests for Controller layer
- Having integration tests using test containers and mocking where needed

# Deployment
All applications will be deployed on Kubernetes and packaged as Docker images


## For Running the app on local
### Step 1: run the below command inside the project folder:

```
mvn clean install
```
It will generate a target folder with application jar in it


### Step 2: Run the below command to tag and build the docker image

```
docker build --build-arg="registry=docker.io/library" --build-arg="version=0.0.1-SNAPSHOT"  -t <your_dockerhub_id>/<app_name> -f src/main/docker/Dockerfile .
```
e.g.
```
docker build --build-arg="registry=docker.io/library" --build-arg="version=0.0.1-SNAPSHOT"  -t online-movie-app/theatre-service -f src/main/docker/Dockerfile .
```

Don't miss the dot in the end of above mentioned command.

### Step 3: To run the app on your local docker instance execute below command
```
docker run -p 8080:8080 <your_dockerhub_id>/blogging-app
```

### Step 4: Now you can access the app on your browser or rest client on localhost:8080. Sample endpoint is
```
GET: localhost:8080/posts
```
