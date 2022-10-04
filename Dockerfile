FROM openjdk:11
RUN mkdir linebot
ADD target/linebot-1.0.0.jar /linebot
WORKDIR /linebot
EXPOSE 8081
ENV SPRING_PROFILES_ACTIVE = 

ENTRYPOINT ["java","-jar","linebot-1.0.0.jar"] 