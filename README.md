# course-except
无课表应用rest风格后台接口
# 项目组织
- 多模块springboot项目
- 依赖fehead-common模块
- 需要额外模块course-except-root作为父模块组织起该项目
- 父模块pom文件如下，**注意packaging为pom**
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.6.RELEASE</version>
        <relativePath/>
    </parent>
    <packaging>pom</packaging>
    <groupId>com.fehead</groupId>
    <artifactId>course-except-root</artifactId>
    <version>1.2-SNAPSHOT</version>

    <modules>
        <module>../course-except</module>
        <module>../fehead-common</module>
    </modules>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>2.3.2</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

## docker容器启动脚本

### 后台服务
```
docker run 
-d 
-p 8888:80 #端口映射
--name course_except_java_${version} #容器名
lmwis/course_except #镜像名(不加tag默认为lastest)
```
### nginx静态资源

```
docker run 
-d 
-p 8082:80 #端口映射
-v /home/nginx_client/course_except/${version}/conf/nginx.conf:/etc/nginx/nginx.conf #全局配置文件 
-v /home/nginx_client/course_except/${version}/html:/usr/share/nginx/html #资源文件
-v /home/nginx_client/course_except/${version}/logs:/var/log/nginx #日志文件
-v /home/nginx_client/course_except/${version}/conf.d:/etc/nginx/conf.d #项目配置文件
--name course_except_nginx_${version} #容器名
nginx #镜像名(不加tag默认为最新版)
```