package br.jus.tjro.gabinete.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JuizLeigoEnvironment {

    private static List<String> tiposJuizLeigo = new ArrayList<>();

    public JuizLeigoEnvironment(@Value("#{'${colegiado.tipos_juizLeigo:8130096}'.split(',')}") List<String> tiposJuizLeigo) {
        this.tiposJuizLeigo = tiposJuizLeigo;
    }

    public static Boolean isMinutaJuizLeigo(String tipoDocId) {
        return tipoDocId != null && tiposJuizLeigo.contains(tipoDocId);
    }
}
