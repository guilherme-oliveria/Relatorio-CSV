package br.jus.tjro.gabinete;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GabineteApplicationTests {

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private Processo getProcesso() {
        Processo processo;
        OrgaoJulgador orgaojulgador = getOrgaoJulgador();
        processo = new Processo(1l);
        processo.setDataEntrada(LocalDateTime.now());
        processo.setId(666l);
        processo.setIdProcessoSistemaLegado(999l);
        processo.setJusticaGratuita(true);
        processo.setNumeroProcesso("7777.1-6");
        processo.setOrgaoJulgadorObj(orgaojulgador);
        processo.setPossuiLiminar(true);
        processo.setPrioridade(true);
        processo.setSegredoJustica(true);
        processo.setSistema(FonteDadosEnum.PJEPG);
        return processo;
    }

    private Minuta getMinuta() {
        Minuta minuta;
        Processo processo = getProcesso();
        minuta = new Minuta(processo);
        minuta.setId(1L);
        minuta.setMinutaHtml("html");
        minuta.setTipoDocumento(getTipoDocumento());
        return minuta;
    }

    private TipoDocumento getTipoDocumento() {
        TipoDocumento tipoA;
        tipoA = new TipoDocumento("1","Despacho",true,false);
        return tipoA;
    }

    private OrgaoJulgador getOrgaoJulgador() {
        OrgaoJulgador orgaojulgador;
        orgaojulgador = new OrgaoJulgador("PJEPG-88");
        orgaojulgador.setDescricao("Descricao");
        orgaojulgador.setSigla("PVH");
        return orgaojulgador;
    }

    @Test
    public void orgaoJulgadorTest() {
        OrgaoJulgador orgaojulgador = getOrgaoJulgador();
        assertEquals("PJEPG-88 | Descricao | PVH ",
            String.format("%s | %s | %s ", orgaojulgador.getId(), orgaojulgador.getDescricao(),
                orgaojulgador.getSigla()));
    }

    @Test
    public void processoTest() {
        Processo processo = getProcesso();
        OrgaoJulgador orgaojulgador = getOrgaoJulgador();
        assertEquals(orgaojulgador.getId(), processo.getOrgaoJulgadorObj().getId());
    }

    @Test
    public void minutaTest() {
        Minuta minuta = getMinuta();
        String comp = "1 | html | 1";
        String obj = String.format("%s | %s | %s", minuta.getId(),
            minuta.getMinutaHtml(), minuta.getTipoDocumento().getId());

        assertEquals(comp, obj);
    }
}
