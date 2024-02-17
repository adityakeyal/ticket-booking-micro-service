ARG registry
FROM ${registry}/openjdk:17-jdk-alpine

ARG user=theatre-user
ARG group=theatre-group
ARG gid=102
ARG uid=1012

ARG service=theatre-service
ARG version

USER root
RUN apk update && apk upgrade

RUN addgroup -S ${group} -g ${gid} && adduser -S ${user} -G ${group} -u ${uid}

USER ${user}

COPY --chown=${user}:${group} target/${service}-${version}.jar /opt/lib/app.jar
COPY --chown=${user}:${group} src/main/docker/*.sh /opt/bin/

RUN dos2unix /opt/bin/*.sh
RUN chmod 755 /opt/bin/*.sh


