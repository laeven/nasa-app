VERSION:=1.0-SNAPSHOT

.PHONY: build run run-local docker

run-local:
	mvn spring-boot:run

build:
	mvn clean install spring-boot:repackage

run:
	java -jar target/nasa-app-$(VERSION).jar

docker: build
	docker build -t nasa-app:latest --build-arg "VERSION=$(VERSION)" .
	docker run -v $(CURDIR)/dates.txt:/app/dates.txt \
		-v $(CURDIR)/config.json:/app/config.json \
		-p 8080:8080 -it \
		nasa-app:latest