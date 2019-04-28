FROM ubuntu:18.04
MAINTAINER Neal Zhu <nealzyf@163.com>

ENV http_proxy http://www-proxy.ericsson.se:8080

RUN set -x \
    && apt-get update && apt-get install -y apt-utils unzip wget curl jq coreutils vim --no-install-recommends \
    && rm -rf /var/lib/apt/lists/*
    
	
ADD jdk-8u211-linux-x64.tar.gz /usr/local/java/


# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/local/java/jdk1.8.0_211
ENV JRE_HOME $JAVA_HOME/jre
ENV PATH $PATH:$JAVA_HOME/bin
# Define default command.
CMD ["bash"]
