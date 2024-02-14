.PHONY: build
build:
	mvn install

.PHONY: run
run:
	mvn javafx:run

.PHONY: clean
clean:
	mvn clean
