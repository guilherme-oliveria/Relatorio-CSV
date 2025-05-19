package br.jus.tjro.gabinete.service.importacao;

import br.jus.tjro.gabinete.model.gab.tag.TagImport;
import br.jus.tjro.gabinete.exceptions.CabecalhoInvalidoException;
import br.jus.tjro.gabinete.exceptions.service.local.CampoInvalidoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Component
public class ImportaTagCsv {
    private static final Logger looger = LoggerFactory.getLogger(ImportaTagCsv.class);

    public static final String CABECALHO_NUMERO_PROCESSO = "Numero_Processo,Nome_Tag,Cor_Hexadecimal,Aviso,Link,Apenas_Gabinete";
    public static final String CABECALHO_NUMERO_OAB = "Numero_OAB,Sigla_Estado,Nome_Tag,Cor_Hexadecimal,Aviso,Link,Apenas_Gabinete";


    public ImportaTagCsv() {
    }

    public static List<TagImport> lerArquivo(final InputStream arquivoInput) throws CabecalhoInvalidoException, CampoInvalidoException {
        looger.info("Processando o arquivo");

        var listTags = new ArrayList<TagImport>();
        try (var scanner = new Scanner(arquivoInput)) {
            scanner.useDelimiter("\n");
            var cabecalho = scanner.next();
            boolean cabecalhoOab = validarCabecalho(cabecalho);
            int count=1;
            while (scanner.hasNext()) {
                adicionarCliente(scanner.next(), listTags,count,cabecalhoOab);
                count++;
            }
        }
        return listTags;
    }

    private static boolean validarCabecalho(String cabecalho) throws CabecalhoInvalidoException {
        if ((cabecalho == null || cabecalho.isEmpty()) || !CABECALHO_NUMERO_PROCESSO.replaceAll("\\s+", "").toLowerCase().equals(cabecalho.replaceAll("\\s+", "").toLowerCase())) {
            if ((cabecalho == null || cabecalho.isEmpty()) || !CABECALHO_NUMERO_OAB.replaceAll("\\s+", "").toLowerCase().equals(cabecalho.replaceAll("\\s+", "").toLowerCase())) {
                throw new CabecalhoInvalidoException("\nCabeçalho não segue o padrão " + CABECALHO_NUMERO_PROCESSO +"\n ou "+ CABECALHO_NUMERO_OAB);
            }
        }

        return CABECALHO_NUMERO_OAB.replaceAll("\\s+", "").toLowerCase().equals(cabecalho.replaceAll("\\s+", "").toLowerCase());
    }

    private static void adicionarCliente(String linha, List<TagImport> tags, int count, boolean cabecalhoOab) throws CampoInvalidoException {
        var campos = linha.split(",");

        if(cabecalhoOab){
            if (!validarCamposOab(campos)) {
                throw new CampoInvalidoException("\nLinha "+count+" Campos obrigatórios não preenchido {} "+ linha);
            } else {
                tags.add(new TagImport(getOrNull(campos,0).trim(),getOrNull(campos,1).trim(),getOrNull(campos,2).trim(), getOrNull(campos,3).trim(),getOrNull(campos,4).trim(), getOrNull(campos,5).trim(), getOrNull(campos,6).trim(),count));
            }
        }else{
            if (!validarCampos(campos)) {
                throw new CampoInvalidoException("\nLinha "+count+" Campos obrigatórios não preenchido {} "+ linha);
            } else {
                tags.add(new TagImport(getOrNull(campos,0).trim(),getOrNull(campos,1).trim(), getOrNull(campos,2).trim(),getOrNull(campos,3).trim(), getOrNull(campos,4).trim(), getOrNull(campos,5).trim(),count));
            }
        }
    }

    private static boolean validarCampos(String[] campos) {
        if (campos.length < 2 || campos[0].isEmpty() || campos[1].isEmpty()) {
            return false;
        }
        return true;
    }

    private static boolean validarCamposOab(String[] campos) {
        if (campos.length < 3 || campos[0].isEmpty() || campos[1].isEmpty() || campos[2].isEmpty()) {
            return false;
        }
        return true;
    }


    public static <T> T getOrNull(T[] array, int index) {
        if (array == null || index < 0 || index >= array.length) {
            return null;
        }
        return array[index];
    }
}
