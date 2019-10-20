VERSION:=1.0-SNAPSHOT

run-local:
	mvn spring-boot:run

build:
	mvn clean install spring-boot:repackage

run:
	java -jar target/nasa-app-$(VERSION).jar

docker: build
	docker build -t nasa-app:latest --build-arg "VERSION=$(VERSION)" .
	docker run -v $(pwd)/config.json:/app/config.json -it nasa-app:latest