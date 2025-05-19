package br.jus.tjro.gabinete.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

@Component
public class ColegiadoEnvironment {
    public static final String ID_RELATORIO = "73";
    public static final String ID_VOTO = "72";
    public static final String ID_EMENTA = "77";
    public static final String ID_ACORDAO = "74";
    public static final String ID_PAUTA = "63";

    private static List<String> tiposDocumento = new ArrayList<>();
    private static List<String> tiposAnexos = new ArrayList<>();
    private static String tipoPreliminar;

    @Autowired
    public ColegiadoEnvironment(
        @Value("#{'${colegiado.tipos_documentos:-1}'.split(',')}") List<String> tiposDocumento,
        @Value("#{'${colegiado.tipos_anexos:-1}'.split(',')}") List<String> tiposAnexos,
        @Value("#{'${colegiado.tipo_preliminar:-1}'}") String tipoPreliminar) {
        this.tiposDocumento = tiposDocumento;
        this.tiposAnexos = tiposAnexos;
        this.tipoPreliminar = tipoPreliminar;
    }

    public static Boolean isMinutaColegiado(String tipoDocId) {
        return tipoDocId != null && tiposDocumento.contains(tipoDocId);
    }

    public static Boolean isAnexoColegiado(String tipoDocId) {
        return tipoDocId != null && tiposAnexos.contains(tipoDocId);
    }
    public static Boolean isPreliminar(String tipoDocId) {
        return ofNullable(tipoDocId).map(it -> it.equals(tipoPreliminar)).orElse(false);
    }

    public static Boolean isRelatorio(String tipoDocId) {
        return ID_RELATORIO.equals(tipoDocId);
    }
}
