# 基础镜像
FROM java:8
# 作者信息
LABEL maintainer="luchunlei42@gmail.com"
# 开放端口
EXPOSE 8087
# 将 ams-0.0.1-SNAPSHOT.jar 复制到 docker 中，并重命名为 ams.jar
ADD /target/bili-reply-1.0-SNAPSHOT.jar bili-reply.jar
# 容器执行命令
ENTRYPOINT ["java", "-jar", "bili-reply.jar"]
