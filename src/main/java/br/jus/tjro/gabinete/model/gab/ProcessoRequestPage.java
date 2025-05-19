package br.jus.tjro.gabinete.model.gab;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessoRequestPage{

    private final int number;
    private final Processo processo;


    public ProcessoRequestPage(@JsonProperty("processo") Processo processo,
                               @JsonProperty("number") int number) {
        this.processo = processo;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public Processo getProcesso() {
        return processo;
    }
}
