package br.jus.tjro.gabinete.model.docs;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static java.util.stream.Stream.of;
import static java.util.stream.Stream.ofNullable;

@Component
public class ModeloDocumentoEnv {
    private final String backurl;
    private final String fronturl;

    public ModeloDocumentoEnv(@Value("${MODELO_DOCUMENTO_HOST:}") String backUrlOld,
                              @Value("${modelodocumento.back.host:}") String backUrl,
                              @Value("${modelodocumento.front.host:}") String frontUrl) {
        this.backurl = of(backUrl, backUrlOld)
            .filter(s -> !s.isBlank())
            .findFirst()
            .orElse("http://localhost:9765");

        this.fronturl = frontUrl;

    }

    public String getBackUrl() {
        return backurl;
    }

    public String getFrontUrl() {
        return fronturl;
    }

    @JsonGetter
    public boolean temFront() {
        return ofNullable(fronturl).anyMatch(s -> !s.isBlank());
    }
}
