dev:
	./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

test:
	./mvnw test -Dspring.profiles.active=test


compose-build:
	mvn clean install
	docker-compose -f docker-compose.yml up

compose-up:
	docker-compose -f docker-compose.yml up

compose-down:
	docker-compose -f docker-compose.yml down
