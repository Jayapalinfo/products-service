# Products Service API

This project was generated with [Spring Initializr](https://start.spring.io/) using following versions.

1. SpringBoot version 2.6.3.
2. Java 11.
3. Maven
4. SQL Server

Simple backend application using REST with following endpoints,

- GET /api/products (get a list of products)
- GET /api/products/1 (get one product from the list)
- PUT /api/products/1 (update a single product)
- POST /api/products (create a product)

All the API's are running behind security. Use below api to get the basic JWT token.
- GET /api/generate-token

## Prerequisites
In order to run this container we need docker installed.

* [Windows](https://docs.docker.com/windows/started)
* [Linux](https://docs.docker.com/linux/started/)

## Local Setup
Import the project in to IDE and go to the application root folder then run `mvn clean install`.

## Usage

Simply build the image using docker. Ggo to the application root folder

#### Run

```shell
docker-compose up
```

## Other Notes

```shell
docker build -t products-service .
docker image build -t products-service .
docker container run --name products -p 8080:8080 -d products-service
docker container logs products
docker pull mcr.microsoft.com/mssql/server
docker run -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=******" -p 127.0.0.1:1433:1433 --name productsDb -d mcr.microsoft.com/mssql/server:2019-latest
```
