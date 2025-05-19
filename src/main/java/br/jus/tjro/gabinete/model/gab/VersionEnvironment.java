package br.jus.tjro.gabinete.model.gab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.time.LocalDateTime;

@Configuration
@PropertySource("classpath:/version.properties")
public class VersionEnvironment {
    @Value("${major}")
    private String major;
    @Value("${minor}")
    private String minor;
    @Value("${patch:#{0}}")
    private String patch;
    @Value("${timestamp:#{''}}")
    private String timestamp;
    @Value("${commit_sha:#{'w1nd50n'}}")
    private String commitSha;

    public String getTimestamp() {
        if (this.timestamp.isEmpty())
            this.timestamp = LocalDateTime.now().toString();
        return this.timestamp;
    }

    public Version getVersion() {
        return new Version(String.format("%s.%s.%s", this.major, this.minor, this.patch), this.getTimestamp(), this.commitSha);
    }
}
