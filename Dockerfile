#stage 1
#Start with a base image containing Java runtime
FROM openjdk:8-slim as build

LABEL maintainer="w22116972@gmail.com"

# dockerfile-maven-plugin will set this variable
ARG JAR_FILE

# Add the application's jar to the container
COPY ${JAR_FILE} app.jar

#unpackage jar file
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar)

#stage 2
FROM openjdk:8-slim
VOLUME /tmp
#RUN apk update && apk upgrade && apk add netcat-openbsd

#Copy unpackaged application to new container
ARG DEPENDENCY=/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# exec app
#ENTRYPOINT ["java","-jar","/app.jar"]
ENTRYPOINT ["java","-cp","app:app/lib/*","example.spring.license.LicenseServiceApp"]
