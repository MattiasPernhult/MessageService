docker-build:
	docker build -t message-service .

docker-run: docker-build
	docker run -d -p 8080:8080 message-service

docker-run-attach: docker-build
	docker run -it -p 8080:8080 message-service