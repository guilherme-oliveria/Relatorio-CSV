package br.jus.tjro.gabinete.model.gab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated
public class ResponsePage<T> {

    private final List<T> content;
    private final int totalPages;
    private final int totalElements;
    private final int number;


    public ResponsePage(@JsonProperty("content") List<T> content,
                        @JsonProperty("totalPages") int totalPages,
                        @JsonProperty("totalElements") int totalElements,
                        @JsonProperty("number") int number) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.number = number;
    }

    public List<T> getContent() {
        return content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public int getNumber() {
        return number;
    }

    public boolean isLast() {
        return !((number+1)<totalPages);
    }
}
