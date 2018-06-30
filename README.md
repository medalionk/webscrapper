# Webscrapper

Given a link, links per level and maximum level, recursively scrape the text in the webpage of all the links upto the maximum level.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

* [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven 3+](https://maven.apache.org/download.cgi)
* [MySQL](https://www.mysql.com/)


### Configurations

Configure the MySQL database connection in /web-scraper/src/main/resources/application.properties

```
spring.datasource.url={connection url}
spring.datasource.username={username}
spring.datasource.password={password}
```
The thread pool properties can also be configured in the application.properties files

```
core-pool-size={core size}
max-pool-size={max size}
queue-capacity={queue capacity}
```

### Running

After creating the executable jar, run the application using java -jar, as shown below:

```

java -jar web-scraper/target/web-scraper-0.0.1-SNAPSHOT.jar

```
To override the connection configurations in the application.properties file, use:

```

{path to java}/java -Dspring.datasource.url=jdbc:mysql://localhost:3306/scrapper -Dspring.datasource.username={username} -Dspring.datasource.password={password} -Dlogging.path={path to login folder} -jar {path to executable}/web-scraper-0.0.1-SNAPSHOT.jar

```


## Running the tests
Make sure that Maven is installed.     
To run the test, navigate to the project folder and run the command as shown below: 
```

{path to maven}/mvn tests

```

## Deployment

Copy the executable jar to the server and run using a Process Control System application such as [Supervisord](http://supervisord.org/) or using java -jar 

## API Reference

[API documentation on Apiary](https://bn4.docs.apiary.io/#) 
Or
Visit the homepage of running application to view the Swagger API documentation.

## Built With

* [Spring Boot](https://projects.spring.io/spring-boot/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [jsoup](https://jsoup.org/) - Java HTML Parser 
* [Lombok](https://projectlombok.org/) - Automatic generation of getters, setters, equals, hashCode and toString
* [MapStruct](http://mapstruct.org/) - Code generator for mappings between Java bean types.
* [RxJava](https://github.com/ReactiveX/RxJava) - Reactive Extensions for composing asynchronous and event-based programs using observable sequences.

## Contributing


## Versioning

API versioning currently at version 1.0


## Author

* **Bilal Abdullah** - [medalionk](https://github.com/medalionk)


## License

This project is licensed under the Apache License - see the [LICENSE](LICENSE) file for details

## Acknowledgments

