import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties
import java.util.Base64
import javax.swing.JOptionPane
import javax.swing.JPasswordField

plugins {
    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.sonarqube") version "3.0"
    id("jacoco")
    id("java")
    id("org.liquibase.gradle") version "2.2.0"

    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
    kotlin("plugin.jpa") version "1.7.22"
}

apply(plugin = "org.liquibase.gradle")


group = "br.jus.tjro"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_19

val properties = Properties()
val propertiesFile = file("$projectDir/src/main/resources/application.properties")
properties.load(propertiesFile.inputStream())

if(properties["tribunal"].toString().equals("tjmg")) {
    repositories {
        mavenCentral()
        jcenter()
        maven {
            url = uri(properties["nexus"].toString())
        }.isAllowInsecureProtocol = true
    }
} else {
    repositories {
        mavenCentral()
        jcenter()
        maven {
            url = uri("http://nexus.tjro.jus.br/repository/maven-public/")
            credentials {
                val properties = Properties()
                val userPropertiesFile = file("$projectDir/src/main/resources/user.properties")
                if (!userPropertiesFile.isFile) {
                    val console = System.console()
                    if (console != null) {
                        username = console.readLine("> Please enter your username: ")
                        password = console.readPassword("> Please enter your password: ").toString()
                    } else {
                        val user = JOptionPane.showInputDialog("Enter User:", "")
                        val passPanel = javax.swing.JPasswordField()
                        val isOk = JOptionPane.showConfirmDialog(null, passPanel, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE)
                        username = user
                        if (isOk == JOptionPane.OK_OPTION) {
                            val pass = String(passPanel.password)
                            password = pass
                        }
                    }

                    properties["user"] = username
                    properties["password"] = Base64.getEncoder().encodeToString(password?.toByteArray())
                    properties.store(userPropertiesFile.writer(), null)
                }
                properties.load(userPropertiesFile.inputStream())
                username = properties["user"] as String
                password = String(Base64.getDecoder().decode(properties["password"] as String))
            }
        }.isAllowInsecureProtocol = true
    }
}

extra["springCloudVersion"] = "2022.0.1"


dependencies {
    implementation("commons-httpclient:commons-httpclient:3.1")
    implementation("org.codehaus.janino:janino:3.1.0")
    implementation("org.codehaus.janino:commons-compiler:3.1.0")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//    implementation("org.springframework.boot:spring-boot-starter-data-redis")
//    org.springframework.boot:spring-boot-starter-data-redis
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-oauth2-resource-server")
    implementation("org.springframework.security:spring-security-oauth2-jose")
    implementation("org.projectlombok:lombok:1.18.28")
    implementation("org.keycloak:keycloak-spring-boot-starter:21.0.2")
    implementation("org.json:json:20230227")
    implementation("com.eatthepath:java-otp:0.4.0")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")

    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")
    implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka")

    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("com.tngtech.archunit:archunit-junit5:0.14.1")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.springframework.security:spring-security-test")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("br.com.six2six:fixture-factory:3.1.0")


//    testImplementation("io.zonky.test:embedded-postgres:2.0.3")
    testImplementation("io.zonky.test:embedded-database-spring-test:2.2.0")
    testImplementation("io.zonky.test:embedded-postgres:2.0.3")

//    testImplementation("com.h2database:h2:2.1.214")
//    <dependency>
//    <groupId></groupId>
//    <artifactId></artifactId>
//    <version></version>
//    <scope>test</scope>
//    </dependency>

    testImplementation("io.cucumber:cucumber-java8:7.0.0")
    testImplementation("io.cucumber:cucumber-junit:7.0.0")
    testImplementation("info.cukes:gherkin:2.12.2")

    implementation("io.minio:minio:5.0.1")
    implementation("org.amshove.kluent:kluent:1.64")
    implementation("io.lettuce:lettuce-core:6.0.2.RELEASE")
    implementation("org.jsoup:jsoup:1.10.2")
    implementation("br.jus.tjro:assinatura-digital-utils:1.0.2")
    implementation("br.jus.tjro:validador-certificado-api:1.0.0")
    implementation("br.jus.tjro:gabinete-api:2.1.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.9.0")
    implementation(fileTree("libs"))
    implementation("org.jboss.resteasy:resteasy-client:3.1.4.Final")
    implementation("org.jboss.resteasy:resteasy-jackson-provider:3.1.4.Final")
    implementation("com.auth0:java-jwt:4.4.0") {
        exclude(group = "org.bouncycastle", module = "bcprov-jdk15on")
    }
    implementation("com.itextpdf:kernel:7.0.5")
    implementation("com.itextpdf:io:7.0.5")
    implementation("com.itextpdf:layout:7.0.5")
    implementation("com.itextpdf:html2pdf:1.0.2")
    implementation("com.github.pukkaone:logback-gelf:1.1.11")
    implementation("org.codehaus.janino:janino:3.1.0")
    implementation("br.jus.tjro:sinapses-api:2.3.0")
    implementation("org.postgresql:postgresql:42.5.4")
    implementation("org.apache.commons:commons-text:1.4")
    implementation("org.pcollections:pcollections:2.1.3")

    implementation("uk.org.lidalia:sysout-over-slf4j:1.0.2")
    implementation("io.vavr:vavr:0.9.2")
    implementation("org.postgresql:postgresql")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.5")

    implementation("org.apache.tika:tika-core:2.0.0")
    implementation("org.skyscreamer:jsonassert:1.5.0")
    implementation("org.liquibase:liquibase-core")
    liquibaseRuntime("info.picocli:picocli:4.6.3")
    liquibaseRuntime("org.liquibase:liquibase-core")
    liquibaseRuntime("org.liquibase:liquibase-gradle-plugin:2.2.0")
    liquibaseRuntime("org.liquibase.ext:liquibase-hibernate5:3.6")
    liquibaseRuntime("org.liquibase:liquibase-groovy-dsl:2.0.1")
    liquibaseRuntime("ch.qos.logback:logback-core:1.2.3")
    liquibaseRuntime("ch.qos.logback:logback-classic:1.2.3")
    liquibaseRuntime("org.postgresql:postgresql")
    liquibaseRuntime("org.yaml:snakeyaml:1.17")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

liquibase {
    val properties = Properties()
    properties.load(file("$projectDir/src/main/resources/application.properties").inputStream())
    val url = properties["spring.datasource.url"] as String
    val username = properties["spring.datasource.username"] as String
    val password = properties["spring.datasource.password"] as String

    activities.register("main") {
        this.arguments = mapOf(
            "logLevel" to "info",
            "changeLogFile" to "./db/changelog/db.changelog-master.yaml",
            "url" to url,
            "username" to username,
            "password" to password
        )
    }
    runList = "main"
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (this.requested.group == "org.apache.logging.log4j") {
            this.useVersion("2.17.1")
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "16"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named("jacocoTestReport", JacocoReport::class) {
    group = "Reporting"
    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(true)
        html.outputLocation.set(file("$buildDir/jacocoHtml"))
        xml.outputLocation.set(file("$buildDir/jacoco.xml"))
        csv.outputLocation.set(file("$buildDir/jacocoCsv.xml"))
    }

    classDirectories.from(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "br/jus/tjro/gabinete/config/**",
                    "br.jus.tjro.gabinete.exceptions/**",
                    "br/jus/tjro/gabinete/controller/**",
                    "br.jus.tjro.gabinete.repository/**",
                    "br.jus.tjro.gabinete.repository/gab/**",
                    "br.jus.tjro.gabinete.repository/webjud/**",
                    "br/jus/tjro/gabinete/controller/**/*",
                    "br/jus/tjro/gabinete/controller/**/**",
                    "br/jus/tjro/gabinete/service/remoto/**",
                    "br/jus/tjro/gabinete/service/remoto/**/*",
                    "br/jus/tjro/gabinete/service/remoto/tpu/**",
                    "br/jus/tjro/gabinete/service/remoto/**/**"
                )
            }
        })
    )
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

