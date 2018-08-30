FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/arthur-nightmare.jar /arthur-nightmare/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/arthur-nightmare/app.jar"]
