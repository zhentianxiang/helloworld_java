#!/bin/sh
# 启动 Java 应用
java -Dfile.encoding=UTF-8 \
     ${JAVA_OPT} \
     -jar ${APP_HOME}/${JAR_FILE}