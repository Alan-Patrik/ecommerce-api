FROM openjdk:11.0.12-slim-buster

COPY /target/*.jar ecommerce_api.jar

SHELL ["/bin/sh", "-c"]

CMD java -jar ecommerce_api.jar