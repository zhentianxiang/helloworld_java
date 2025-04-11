FROM harbor.linuxtian.com/library/jdk8-maven:latest

MAINTAINER zhentianxiang

ARG PORT=8080
ARG LANG=zh_CN.UTF-8
ARG TZ=Asia/Shanghai
ARG PROFILE=dev

ENV JAVA_OPTS="-Dspring.profiles.active=${PROFILE}"
ENV PORT=${PORT}
ENV USER=root
ENV APP_HOME=/home/${USER}/apps
ENV JAR_FILE=app.jar
ENV LANG=${LANG}
ENV TZ=${TZ}

COPY target/*.jar ${APP_HOME}/${JAR_FILE}
COPY start.sh ${APP_HOME}/start.sh

WORKDIR ${APP_HOME}

CMD ["sh", "-c", "/bin/sh $APP_HOME/start.sh"]
