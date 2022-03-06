## Simple File Storage server Rest API 


* Tested only in windows 10

**Prerequisites**

1. Java - 1.8.x

2. Maven - 3.x.x


### Steps to Setup

**1. Building and running Test cases** 

Go to Project Root Folder and run

```
mvn clean test
```

**2. Code quality/coverage check using Jococo**

Go to Project Root Folder and run following commands

```
mvn clean test

```

```
mvn jacoco:report

```

Go to 
target\site\jacoco and open open HTML file index.html to see the Coverage report
```

**2. Run the app using maven**

```
mvn spring-boot:run

```

That's it! The REST application will be started on port `8080`.

We can also package the application in the form of a jar and then run the jar file as

```
mvn clean package
java -jar target/fileserver-0.0.1-SNAPSHOT.jar

```

**3. Future Enhancements**

1. API Version .I dont wanted to introduce a version via HATEOS or HEADER or as PATH variable. None of them are scalable wrt to client point of view. Ideally giving different urls will be ideal

1. Swagger REST API - Already code changes are done. Need to make changes in maven (POM.xml)

2. Sonar Qube Integration 

3. Jenkins Pipeline

4. Kubernetes ready (for autoscaling and hot deployment)

5. cloud native /Infrastructure as code