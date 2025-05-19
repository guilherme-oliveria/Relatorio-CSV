package br.jus.tjro.gabinete.model.gab.question;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Option {
    private final String key;
    private final String value;

    @JsonCreator
    Option(@JsonProperty("key") String key, @JsonProperty("value") String value){
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option options = (Option) o;
        return Objects.equals(key, options.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
