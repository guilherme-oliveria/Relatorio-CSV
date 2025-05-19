FROM ibmcom/curl:4.2.0-build.2 as apm
ENV APM_AGENT_VERSION=1.34.1
RUN curl https://repo1.maven.org/maven2/co/elastic/apm/elastic-apm-agent/${APM_AGENT_VERSION}/elastic-apm-agent-${APM_AGENT_VERSION}.jar --output elastic-apm-agent.jar

FROM eclipse-temurin:19.0.2_7-jdk
VOLUME /tmp
COPY build/libs/*.jar app.jar
COPY --from=apm /home/curler/elastic-apm-agent.jar ./
RUN chmod +x elastic-apm-agent.jar
ENV JAVA_OPTS="$JAVA_OPTS -javaagent:/elastic-apm-agent.jar -Delastic.apm.application_packages=br.jus.tjro"
RUN curl -sSLk https://git.tjro.jus.br/demilitarizedZone/certificado-installer/raw/master/install.sh | sh && \
    which java | xargs -I {} readlink -f {} | sed 's,bin\/java,,' | xargs -I {} find {} -name cacerts | xargs -I {} keytool -import -trustcacerts -alias china -file /china.crt -storepass changeit -keystore {} --noprompt 2> /dev/null || { echo >&2 \"There is no Java installed\"; }
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
