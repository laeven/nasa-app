
build:
	mvn clean compile assembly:single 

run:
	java -jar target/nasa-app-1.0-SNAPSHOT-jar-with-dependencies.jar

docker:
	docker build -t nasa-app:latest .