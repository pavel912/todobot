DEFAULT_GOAL := build-run

clean:
	./gradlew clean

build:
	 ./gradlew clean build test

run:
	./gradlew run

start:
	./gradlew bootRun --args='--spring.profiles.active=dev'

start-prod:
	./gradlew bootRun --args='--spring.profiles.active=prod'

test:
	./gradlew test

update-deps:
	./gradlew useLatestVersions

stage:
	./gradlew stage

build-run: build run

.PHONY: build