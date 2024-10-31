# helloworld_java

## 介绍
1. web访问输出hello world,
2. 并可获取到服务器ip地址和主机名
3. 屏幕不停的输出1000内随机数
4. 适合用jar包做实验，例如用作docker,kubernetes的简单demo使用,以及zabbix监控可获取日志中的随机数用在监控项
5. 本项目也支持gitlab-ci,用于集成gitlab和kubernetes，详情可参考博客[https://www.cnblogs.com/Sunzz/p/13716477.html](https://www.cnblogs.com/Sunzz/p/13716477.html)
## 软件架构

Spring Boot


## 安装教程
#### 下载并运行
1.  git clone https://gitee.com/SunHarvey/helloworld_java.git
2.  cd helloworld_java
3.  java -jar ./target/helloworld-1.0.jar
#### 或者自己打包后运行
1.  git clone https://gitee.com/SunHarvey/helloworld_java.git
2.  cd helloworld_java
3.  mvn clean install
4.  java -jar ./target/helloworld-1.0.jar
