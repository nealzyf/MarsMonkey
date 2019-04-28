FROM nealzyf/java8-with-utils:v1.0
MAINTAINER Neal Zhu <nealzyf@163.com>

ARG JAR_FILE
ADD target/${JAR_FILE} /opt/services/${JAR_FILE}
ADD target/start.sh /opt/services/start.sh
RUN chmod 777 opt/services/start.sh

CMD ["/opt/services/start.sh","start"]