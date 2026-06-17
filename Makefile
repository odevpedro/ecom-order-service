.PHONY: build test run docker-build

build:
	./mvnw package -DskipTests

test:
	./mvnw test

run:
	./mvnw spring-boot:run

docker-build:
	docker build -t ecom-order-service .
