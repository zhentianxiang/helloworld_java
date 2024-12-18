FROM registry.cn-hangzhou.aliyuncs.com/tianxiang_app/jdk-8u421:v3

MAINTAINER SunHarvey

ENV USER root

ENV APP_HOME /home/$USER/apps

ENV JAR_FILE=app.jar

COPY target/*.jar ${APP_HOME}/${JAR_FILE}

COPY start.sh ${APP_HOME}/start.sh

WORKDIR ${APP_HOME}

CMD ["sh", "-c", "/bin/sh $APP_HOME/start.sh"]
