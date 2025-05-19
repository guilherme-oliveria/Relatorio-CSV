package br.jus.tjro.gabinete.service.local;
import br.jus.tjro.gabinete.exceptions.TagDeSistemaException;
import br.jus.tjro.gabinete.exceptions.service.local.ProcessoParteServiceException;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoTag;
import br.jus.tjro.gabinete.model.gab.tag.Tag;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.repository.gab.TagRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoTagRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.service.importacao.ObtemItensParaAdicionarAtualizarOuExcluir;
import br.jus.tjro.gabinete.service.remoto.TagRemotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService {

    protected TagRepository repository;

    protected ProcessoTagRepository processoTagRepository;

    protected ProcessosRepository processoRepository;

    private TagRemotoService tagRemotoService;

    private final String nomeTagValidarExpCartorio;
    private final String corTagValidarExpCartorio;

    @Value("${TAG_COR_PJE:#5bc0de}")
    private String corTagPje;

    @Autowired
    public TagService(@Value("${TAG_VALIDA_EXPEDIENTE_CARTORIO:Validar Expediente Cartório|#8800df}") String tagValidaExpCartorio,TagRepository repository,
                      ProcessoTagRepository processoTagRepository, TagRemotoService tagRemotoService, ProcessosRepository processoRepository) {
        this.repository = repository;
        this.processoTagRepository = processoTagRepository;
        this.tagRemotoService = tagRemotoService;
        String[] split = tagValidaExpCartorio.split("\\|");
        this.nomeTagValidarExpCartorio = split[0];
        this.corTagValidarExpCartorio = split[1];
        this.processoRepository = processoRepository;
    }

    public List<Tag> obterPorOrgaoJulgador(String orgaoJulgador, Usuario usuario) throws Exception {
        if (orgaoJulgador.equals("-1")) {
                List<String> ojs = usuario.getOrgaosJulgadoresCompleto().stream()
                    .map(o -> {
                        return o.getId().toLowerCase();
                    })
                    .collect(Collectors.toList());
                ojs.add("-1");
            return repository.findByOrgaosJulgadores(ojs);
        }
        return repository.findByOrgaoJulgador(orgaoJulgador);
    }

    public List<Tag> obterPorOrgaoJulgador(String orgaoJulgador) throws Exception {
        return repository.findByOrgaoJulgador(orgaoJulgador);
    }

    public Tag obterPorOrgaoJulgadorEId(String orgaoJulgador, Long id) {
        List<Tag> tags = repository.findByOrgaoJulgadorAndId(orgaoJulgador, id);
        if(tags.isEmpty())
            return null;
        else
            return tags.get(0);
    }

    public Tag verificarSeExisteESalvar(String descricao, String oj) {
        var tags = repository.findByTagAndOrgaoJulgador(descricao, oj);
        if(tags.isEmpty())
            return save(new Tag(descricao, "#ff0000", oj));
        else
            return tags.get(0);
    }

    public Tag verificarSeExisteESalvarComCor(String descricao, String oj, String cor, Integer idTagPje) {
        List<Tag> tags;
        tags = repository.findByTagAndOrgaoJulgador(descricao, oj);
        Tag tag;
        if(tags.isEmpty()) {
            tags = repository.findByTagAndOrgaoJulgador(descricao, "-1");
            if(!tags.isEmpty()) {
                tag = tags.get(0);
            } else {
                tag = new Tag(descricao, cor, oj);
            }
            tag.setOrgaoJulgador(oj);
            tag.setIdTagPje(idTagPje);
            return save(tag);
        } else {
            tag = tags.get(0);
            tag.setCorHexadecimal(cor);
            return repository.save(tag);
        }
    }

    public Tag verificarSeExisteESalvar(Tag tagObj) {
        var tags = repository.findByTagAndOrgaoJulgador(tagObj.getTag(), tagObj.getOrgaoJulgador());
        if(!tags.isEmpty()) {
            Tag tag = tags.get(0);
            tag.setTag(tagObj.getTag());
            tag.setCorHexadecimal(tagObj.getCorHexadecimal());
            tag.setAviso(tagObj.getAviso());
            tag.setUrl(tagObj.getUrl());
            tag.setApenasMeuGabinete(tagObj.getApenasMeuGabinete());
            return this.save(tag);
        } else {
            return save(tagObj);
        }
    }

    public Tag save(Tag tag)  {
        return repository.save(tag);
    }

    public void delete(long tagId, Usuario usuarioLogado) throws Exception {
        var tag = repository.findById(tagId);
        if (tag.isPresent()) {
            if(tag.get().isDeSistema() || tag.get().getIdTagPje()!=null) {
                throw new TagDeSistemaException();
            }
            List<ProcessoTag> processoTags = processoTagRepository.findAllByTag(tag.get());
            processoTags.forEach(t -> t.deletar(usuarioLogado.getNome()));
            processoTagRepository.saveAll(processoTags);
            repository.delete(tag.get(), usuarioLogado);
        }
    }

    public Optional<ProcessoTag> getTagValidarExpediente(Processo processo) {
        List<Tag> tags = repository.findByTagAndOrgaoJulgador(this.nomeTagValidarExpCartorio, Tag.OrgaoTagTipoSistema);
        if(tags.isEmpty())
            repository.save(new Tag(this.nomeTagValidarExpCartorio, this.corTagValidarExpCartorio, Tag.OrgaoTagTipoSistema));
        ProcessoTag processoTag = processoTagRepository.findByProcessoAndTag(processo, tags.get(0));
        if(processoTag != null) {
            return Optional.empty();
        } else {
            processoTag = processoTagRepository.save(new ProcessoTag(tags.get(0), false, processo));
            return Optional.of(processoTag);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void importaOuAtualizaTagsDoProcesso(Processo processo, List<String> listTag) throws Exception {

        Long idLegado = processo.getIdProcessoSistemaLegado();
        FonteDadosEnum fonte = processo.getSistema();
        List<Tag> tagsRemoto;
        try{
            tagsRemoto =  tagRemotoService.getTagPorProcessoId(idLegado, fonte);
            for(Tag ta: tagsRemoto){
                ta.setOrgaoJulgador(processo.getOrgaoJulgador());
            }
        }catch (HttpServerErrorException e){
            throw new Exception("Erro ao buscar tags no servidor remoto! O servidor diz: "+e.getResponseBodyAsString(),e);
        }
        List<ProcessoTag> processoTagsLocal = processoTagRepository.findByProcesso(processo);

        ObtemItensParaAdicionarAtualizarOuExcluir partesAddOuAtt = new ObtemItensParaAdicionarAtualizarOuExcluir(processoTagsLocal,tagsRemoto);

        List<ProcessoTag> atualizar = new ArrayList<>();

        partesAddOuAtt.getListaAtualizacao().forEach((o1, o2) -> {
            ProcessoTag processoTag = (ProcessoTag) o1;
            Tag tagRemoto = (Tag) o2;
            Tag tagBase = repository.findFirstByIdTagPje(tagRemoto.getIdTagPje());

            tagBase.setTag(tagRemoto.getTag().trim());

            processoTag.setTag(tagBase);
            atualizar.add(processoTag);
        });

        try{
            partesAddOuAtt.getListaAdicao().forEach(o -> {
                Tag remotoTag = (Tag) o;
                Tag tagNew = verificarSeExisteESalvarComCor(remotoTag.getTag().trim(),processo.getOrgaoJulgador(),corTagPje,remotoTag.getIdTagPje());
                ProcessoTag local = new ProcessoTag(tagNew,false, processo);
                listTag.add(remotoTag.getTag());
                saveProcessoTag(local);
            });

            if(atualizar.size() > 0)
                processoTagRepository.saveAll(atualizar);
        }catch (Exception e){
            throw new ProcessoParteServiceException("Erro ao salvar Tags. ID Processo: "+processo.getId()+" "+processo.getNumeroProcesso(),e);
        }

    }

    public ProcessoTag saveProcessoTag(ProcessoTag processoTag) {
        if (!processoTagJaTemTag(processoTag.getProcesso().getId(), processoTag.getTag().getId())) {
            processoTagRepository.save(processoTag);
        }
        return processoTag;
    }

    public Boolean processoTagJaTemTag(Long processoId, Long tagId) {
        Processo processo = processoRepository.findById(processoId).orElse(null);
        Tag tag = this.repository.findById(tagId).orElse(null);
        if (processo != null && tag != null) {
            return processoTagRepository.findAllByProcessoAndTag(processo, tag).size() > 0;
        } else {
            return false;
        }
    }
}
