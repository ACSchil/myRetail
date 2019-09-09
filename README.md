# myRetail RESTful Service

## Introduction

This project is a case study / proof of concept (PoC) for the following.

myRetail is a rapidly growing company with HQ in Richmond, VA and over 200 stores across the east coast. myRetail wants to make its internal data available to any number of client devices, from myRetail.com to native mobile apps. 

The goal for this exercise is to create an end-to-end Proof-of-Concept for a products API, which will aggregate product data from multiple sources and return it as JSON to the caller. 

Your goal is to create a RESTful service that can retrieve product and price details by ID. The URL structure is up to you to define, but try to follow some sort of logical convention.

## PoC Requirements
Build an application that performs the following actions: 
- Responds to an HTTP `GET` request at `/products/{id}` and delivers product data as JSON where `{id}` will be a number. 
    - Example product IDs: `15117729`, `16483589`, `16696652`, `16752456`, `15643793`) 
    - Example response: `{"id":13860428,"name":"The Big Lebowski (Blu-ray) (Widescreen)","current_price":{"value": 13.49,"currency_code":"USD"}}`
- Performs an HTTP `GET` to retrieve the product name from an external API. (For this exercise the data will come from `redsky.target.com`, but let’s just pretend this is an internal resource hosted by myRetail)  
    - Example: `http://redsky.target.com/v2/pdp/tcin/13860428?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics`
- Reads pricing information from a NoSQL data store and combines it with the product ID and name from the HTTP request into a single response.  
- **BONUS:** Accepts an HTTP `PUT` request at the same path (`/products/{id}`), containing a JSON request body similar to the `GET` response, and updates the product’s price in the data store.  

## Building

The application targets Java 8. Installing JDK 8 is a prerequisite to building.

From the project root, run

`./gradlew build` 

which will also print the application version built.

## Testing

Every build runs the tests. 

There are two flavors of tests -- both written using Spock -- unit and integration. Integration tests spin up the Spring Boot application, and make requests against it. External API requests are mocked using WireMock. The integration tests have a dependency on an external Cassandra cluster, see [Using Dockerized Cassandra](#using-dockerized-cassandra) below. 

To run the tests without building: 

`./gradlew test`


## Running

This is a Spring Boot application. After building, run:

`java -jar build/libs/myRetail-{version}.jar`

where `version` comes from the build step. The version can be determined by inspecting `gradle.properties` or looking in `./build/libs/`

### Remote Debugging

For local development, the application can also be run with a remote debugging port (port `5005`). To do this, run:

`./gradlew bootRun --debug-jvm`

and connect to it with your IDE.

### Using Dockerized Cassandra

#### Single Node Cluster

Assuming you have docker installed:
- Create a network: `docker network create myRetailCassandraNetwork`
- Run the container: `docker run --name myRetailCassandra --network myRetailCassandraNetwork -p 9042:9042 -d cassandra:3.11.4`

Now hydrate the data store (assumes cqlsh is installed): 
- Create a keyspace: 
`CREATE KEYSPACE IF NOT EXISTS myRetail WITH REPLICATION = { 
    'class' : 'SimpleStrategy', 
    'replication_factor' : 1 
};`
- Create a new table for price data: 
`CREATE TABLE IF NOT EXISTS myRetail.productPrice (
    productId bigint,
    value decimal,
    PRIMARY KEY (productId)
);`
- Insert a product, e.g.: 
`INSERT INTO productPrice( productId, value ) values( 13860428, 14.99 );`
                                      

## Interacting

The service runs on port `8080`. 

To determine if the application has come up, hit `localhost:8080/probe/liveness` - it should respond with an HTTP status code of `200` and a body of `Alive`

To update a product's price, `PUT` the new price to `/products/{id}`, e.g.

`curl -XPUT -H "Content-Type: application/json" -d '{"value":13.49}' http://localhost:8080/products/13860428`

To fetch an aggregation of the product details and price, perform a `GET` against `/products/{id}`, e.g.

`curl http://localhost:8080/products/13860428`

### Docker

This project can run as a container. The `Dockerfile` builds an image for deployment in a GCP Kubernetes cluster. To build the image, run:

`./gradlew clean build && docker build .`

Then tag the image (to find the image id, run `docker images`):

`docker tag {imageId} {user-name}/my-retail:{version}}`

and push it up to Docker Hub:

`docker push {username}/my-retail`

Here's its current location: https://hub.docker.com/r/sirschilly/my-retail

### Interacting With the Deployed Service

This application has been deployed on Google Kubernetes Engine (GKE). At the time of this writing there is a 3 node Cassandra cluster and a single node deployment of the the myRetail web api. 

It is accessible at: http://34.70.191.115

- Heartbeat: http://34.70.191.115/probes/liveness
- Getting a product: `http://34.70.191.115/products/{id}`, e.g. http://34.70.191.115/products/13860428

## Outstanding Work

- CI/CD pipeline 
- Smoke tests and monitoring/alerting
- Source control for Kubernetes configs
- Kubernetes config for Cassandra cluster should setup tables
- Auto-scaling policies
- Liveness/Readiness probes
- Support for dynamic configuration
- Performance tuning
- Security considerations
    - Move traffic to 443