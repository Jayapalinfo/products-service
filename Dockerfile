From openjdk:11
copy ./target/products-service-0.0.1-SNAPSHOT.jar products-service-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","products-service-0.0.1-SNAPSHOT.jar"]