FROM adoptopenjdk:16-jre-hotspot
WORKDIR /opt/app
COPY target/masterpiece-0.0.1-SNAPSHOT.jar masterpiece.jar
CMD ["java", "-jar", "masterpiece.jar"]