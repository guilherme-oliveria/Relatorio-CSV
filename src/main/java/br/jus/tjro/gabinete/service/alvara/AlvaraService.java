package br.jus.tjro.gabinete.service.alvara;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.alvara.Alvara;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.pessoa.PessoaDocumento;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoTag;
import br.jus.tjro.gabinete.model.gab.tag.Tag;
import br.jus.tjro.gabinete.model.gab.transiente.Partes;
import br.jus.tjro.gabinete.repository.gab.TagRepository;
import br.jus.tjro.gabinete.repository.gab.alvara.AlvaraRepository;
import br.jus.tjro.gabinete.service.local.MinutaService;
import br.jus.tjro.gabinete.service.local.ProcessoParteService;
import br.jus.tjro.gabinete.service.local.ProcessoService;
import br.jus.tjro.gabinete.service.local.ProcessoTagService;
import br.jus.tjro.gabinete.util.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@Service
public class AlvaraService {

    @Autowired
    private AlvaraRepository alvaraRepository;

    @Autowired
    private MinutaService minutaService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProcessoParteService processoParteService;

    @Autowired
    private ProcessoService processoService;

    @Autowired
    private ProcessoTagService processoTagService;

    @Transactional
    public Alvara salvarInformacoesBancariasAlvara(Alvara alvara) throws Exception {
        Minuta minuta = alvara.getMinuta();
        if (!minuta.temAlvara()) {
            alvara.getMinuta().setAlvara(alvara);
        }
        alvara.setDataCadastro(LocalDateTime.now());
        alvara.getPagamentoAlvara().forEach(p -> p.setAlvara(alvara));
        Alvara alvaraSaved = alvaraRepository.save(alvara);
        this.toggleTagAlvara(alvaraSaved);
        return alvaraSaved;
    }

    public static String convertToCamelCase(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }
        return converted.toString();
    }

    public ResponseEntity<?> buscaParteProcessoPorIdProcesso(long idProcesso) throws Exception {
        List<Partes> listPartes = new ArrayList<>();
        Processo processo = processoService.findOne(idProcesso);
        List<ProcessoParte> processoPartes = processoParteService.retornaTodasAsPartesPorProcesso(processo);
        processoPartes.forEach(processoParte -> {
            Partes partes = new Partes(processoParte.getId(), processoParte.getPessoa().getNome(), processoParte.getTipoPolo(), processoParte.getTipoParte(), processoParte.getProcuradoria(), "");
            String documento = "";
            try {
                documento = processoParte.getPessoa().getPessoaDocumentos().stream()
                    .filter(doc -> (doc.getTipoDocumento().equals("CPF") || doc.getTipoDocumento().equals("CPJ")))
                    .findFirst()
                    .map(PessoaDocumento::getDocumento).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            partes.setNrDocumento(documento);
            listPartes.add(partes);
        });
        return !listPartes.isEmpty() ? ok(listPartes) : noContent().build();
    }

    public void toggleTagAlvara(Alvara alvara) throws Exception {
        Minuta minuta = alvara.getMinuta();
        Processo processo = minuta.getProcesso();
        List<Tag> tagsAlvara = tagRepository.findByTagAndOrgaoJulgador("VALIDAR_ALVARA_ELETRONICO", Tag.OrgaoTagTipoSistema);
            if(tagsAlvara.isEmpty())
                tagRepository.save(new Tag("VALIDAR_ALVARA_ELETRONICO", "#00e640", Tag.OrgaoTagTipoSistema));
        Tag tagAlvara = tagsAlvara.get(0);
        boolean temTagAlvara = processoTagService.processoJaTemTag(processo.getId(), tagAlvara.getId());
        boolean alvaraAtivo = minuta.temAlvara();

        if(!temTagAlvara && alvaraAtivo){
            ProcessoTag processoTag = new ProcessoTag(tagAlvara, false, processo);
            processoTagService.save(processoTag);
        }else if(!alvaraAtivo && temTagAlvara) {
            Usuario usuario = AuthenticationUtil.getUsuarioSistema();
            Tag finalTagAlvara = tagAlvara;
            List<ProcessoTag> processoTags = processo.getTags().stream()
                .filter(pt -> pt.getTag().getId().equals(finalTagAlvara.getId()))
                .collect(Collectors.toList());
            processoTags.forEach(t->t.deletar(usuario.getNome()+" CPF"+usuario.getCpf()));
            processoTagService.save(processoTags);
        }
    }
}

