FROM ubuntu

COPY src /app

RUN apt update \
    && apt install -y wget build-essential \
    && wget https://download.java.net/java/GA/jdk19/877d6127e982470ba2a7faa31cc93d04/36/GPL/openjdk-19_linux-x64_bin.tar.gz \
    && mkdir -p /opt/java \
    && tar -xzf openjdk-19_linux-x64_bin.tar.gz --strip-components=1 -C /opt/java \
    && rm -rf openjdk-19_linux-x64_bin.tar.gz \
    && export JAVA_HOME=/opt/java

# Compiling layer
WORKDIR /app
RUN /opt/java/bin/javac --enable-preview --release 19 *.java

CMD ["/opt/java/bin/java", "--enable-preview", "VirtualThreadTest"]


