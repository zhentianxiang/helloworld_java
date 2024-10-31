FROM harbor.meta42.indc.vnet.com/library/jdk8:v4
MAINTAINER SunHarvey
ENV USER centos
ENV APP_HOME /home/$USER/apps/
ADD target/*.jar $APP_HOME
ENTRYPOINT ["java","-Dfile.encoding=UTF-8", "-jar", "helloworld-1.0.jar"]
