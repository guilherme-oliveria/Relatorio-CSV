package br.jus.tjro.gabinete.model.gab;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Audiencia {

    private final String link;
    private final Date dataAudiencia;
    private final String status;

    @JsonCreator
    public Audiencia(@JsonProperty("linkVideoDrs") String link,
                     @JsonProperty("dataAudiencia")
                     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
                         Date dataAudiencia,
                     @JsonProperty("status") String status) {
        this.link = link;
        this.dataAudiencia = dataAudiencia;
        this.status = status;
    }

    public String getLink() {
        return link;
    }

    public Date getDataAudiencia() {
        return dataAudiencia;
    }

    public String getStatus() {
        return status;
    }
}
