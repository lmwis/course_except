# 定义基础镜像
FROM java:8

# 定义维护者信息
MAINTAINER lmwis <lmwis2000ygn@gmail.com>

# 添加宿主机的jdk 到容器中的指定目录
ADD target/course-except-1.4-SNAPSHOT.jar /home/java/app.jar

# 设置容器启动时执行的操作
ENTRYPOINT ["nohup","java","-jar","/home/java/app.jar","&"]



