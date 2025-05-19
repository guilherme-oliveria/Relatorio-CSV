FROM eclipse-temurin:19.0.2_7-jdk
COPY build/libs/*.jar app.jar
RUN curl -sSLk https://git.tjro.jus.br/demilitarizedZone/certificado-installer/raw/master/install.sh | sh && \
    which java | xargs -I {} readlink -f {} | sed 's,bin\/java,,' | xargs -I {} find {} -name cacerts | xargs -I {} keytool -import -trustcacerts -alias china -file /china.crt -storepass changeit -keystore {} --noprompt 2> /dev/null || { echo >&2 \"There is no Java installed\"; }
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
