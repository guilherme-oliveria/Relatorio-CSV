package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.listener.model.processo.listener.LocalizadorListener;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.pessoa.PessoaDocumento;
import br.jus.tjro.gabinete.model.gab.tag.TagImport;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoTag;
import br.jus.tjro.gabinete.model.gab.processo.WrapperTagExterna;
import br.jus.tjro.gabinete.model.gab.tag.Tag;
import br.jus.tjro.gabinete.repository.gab.TagRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoTagRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.service.importacao.ImportaTagCsv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.InputStreamResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ProcessoTagService {

    @Autowired
    protected ProcessoTagRepository repository;

    @Autowired
    protected ProcessosRepository processoRepository;

    @Autowired
    protected TagService tagService;

    @Autowired
    protected TagRepository tagRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private ProcessoParteService processoParteService;

    private ImportaTagCsv importaTagCsv;

    private ProcessosRepository processosRepository;
    private TagImportService tagImportService;
    private PessoaDocumentoService pessoaDocumentoService;

    private final LocalizadorListener localizadorListener;

    public ProcessoTagService(TagImportService tagImportService, ImportaTagCsv importaTagCsv, ProcessosRepository processosRepository, PessoaDocumentoService pessoaDocumentoService, LocalizadorListener localizadorListener) {
        this.importaTagCsv = importaTagCsv;
        this.processosRepository = processosRepository;
        this.tagImportService = tagImportService;
        this.pessoaDocumentoService=pessoaDocumentoService;
        this.localizadorListener = localizadorListener;
    }

    @PreAuthorize("validaProcessoAcessado(#processo)")
    public List<ProcessoTag> obterPorProcesso(Processo processo) {
        return repository.findByProcesso(processo);
    }

    public ProcessoTag save(ProcessoTag processoTag) {
        if (!processoJaTemTag(processoTag.getProcesso().getId(), processoTag.getTag().getId())) {
            repository.save(processoTag);
        }
        return processoTag;
    }

    public List<ProcessoTag> save(List<ProcessoTag> processoTags) {
        processoTags.forEach(p -> {
            if (!processoJaTemTag(p.getProcesso().getId(), p.getTag().getId())) {
                repository.save(p);
            }
        });
        return processoTags;
    }

    public List<Processo> obterProcessosPorTagId(Long tagId) {
        return repository.findByTag_Id(tagId).stream().map(ProcessoTag::getProcesso).collect(Collectors.toList());
    }

    public Boolean processoJaTemTag(Long processoId, Long tagId) {
        Processo processo = processoRepository.findById(processoId).orElse(null);
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (processo != null && tag != null) {
            return this.repository.findAllByProcessoAndTag(processo, tag).size() > 0;
        } else {
            return false;
        }
    }

    public ProcessoTag salvarTagUsandoWrapperExterno(WrapperTagExterna wtc, Processo processo) {
        return this.save(new ProcessoTag(tagService.verificarSeExisteESalvar(wtc.getTag(),
            processo.getOrgaoJulgador()), false, processo));
    }


    @Transactional
    public InputStreamResource importarTagCsv(int tipoAcao, InputStream inputStream, Usuario usuarioLogado) throws Exception{
        List<TagImport> tags = importaTagCsv.lerArquivo(inputStream);
        return processaTagImportProcesso(tipoAcao,tags,true,usuarioLogado.getNome());
    }

    /**
     * verificar tabela tagImport para adicionar ao processo, regra da corregedoria
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public InputStreamResource atualizaTagImportPorProcesso(Processo processo,  List<String> listTag) throws Exception{
        List<TagImport>  tagImports= tagImportService.findByNrProcesso(processo.getNumeroProcesso());

        if(tagImports.isEmpty()) {
            List<PessoaDocumento> pessoaDocumentos = pessoaDocumentoService.findAdvogadoDocumentosPorProcesso(processo);
            for (PessoaDocumento pessoaDocumento : pessoaDocumentos) {
                tagImports.addAll(tagImportService.findByNrOabAndSiglaEstado(pessoaDocumento.getDocumento().replaceAll("[^0-9]+", ""), pessoaDocumento.getUf()));
            }
        }
        tagImports.removeIf(tagImport -> listTag.contains(tagImport.getNomeTag()));
        if(tagImports.isEmpty()){
            return null;
        }else{
            return processaTagImportProcesso(TagImport.TIPO_INCLUIR,tagImports,false,"SISTEMA");
        }
    }

    public InputStreamResource processaTagImportProcesso(int tipoAcao, List<TagImport> tags, boolean incluirTagImport, String usuarioLogado) throws Exception {
        InputStreamResource resource = null;

        if(tipoAcao == TagImport.TIPO_ATUALIZAR){
            List<TagImport> tagImportsAntigoExcluir = tagImportService.findAll();
            processarHashMap(TagImport.TIPO_EXCLUIR,mapProcessoTagImport(tagImportsAntigoExcluir),usuarioLogado);

            processarHashMap(TagImport.TIPO_INCLUIR,mapProcessoTagImport(tags),usuarioLogado);

            tagImportService.deleteAll();
        }else{
            resource = processarHashMap(tipoAcao,mapProcessoTagImport(tags),usuarioLogado);
        }

        if(incluirTagImport){
           if (tipoAcao == TagImport.TIPO_EXCLUIR){
                tags.forEach(obj ->{
                    if(obj.getNrProcesso()!=null){
                        tagImportService.deleteByNrProcessoAndNomeTag(obj);
                    }else{
                        tagImportService.deleteByNrOabAndSiglaEstadoAndNomeTag(obj);
                    }
                });
           }else{
               tags.forEach(obj ->{
                   if(obj.getNrProcesso()!=null) {
                       tagImportService.verificarSeExisteESalvar(obj);
                   }else{
                       tagImportService.verificarSeExisteESalvarOab(obj);
                   }
               });
           }
        }

        return resource;
    }

    private InputStreamResource processarHashMap(int tipoAcao, HashMap<String, List<Tag>> hashMapProcessoTag, String usuarioLogado) throws Exception{
        List<String> listaNaoEncontrado = new ArrayList<>();

        for (String numeroTag : hashMapProcessoTag.keySet()) {
            if (numeroTag.substring(0, 2).matches("[a-zA-Z]+")){
                String numeroOab = numeroTag.replaceAll("[^0-9]+", "");

                List<Processo>   processos = processosRepository.findProcessoByOabAndEstadoAndNumeroDocumento(Integer.parseInt(numeroOab),numeroTag.substring(0, 2));

                if(!processos.isEmpty()){
                    processos.forEach(processo -> {
                        processoTagProcessar(processo,numeroTag,tipoAcao,hashMapProcessoTag,usuarioLogado);
                    });
                }else{
                    listaNaoEncontrado.add(numeroTag);
                }

            }else{
                Processo  processo = processosRepository.findByNumeroProcesso(numeroTag);
                processoTagProcessar(processo,numeroTag,tipoAcao,hashMapProcessoTag,usuarioLogado);
            }
        }

        if(listaNaoEncontrado.size()>0){
            return criarArquivoTxt(listaNaoEncontrado);
        }else{
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private void processoTagProcessar(Processo processo, String numeroTag,int tipoAcao, HashMap<String, List<Tag>> hashMapProcessoTag, String usuarioLogado ){
        if (processo != null) {
            List<Tag> tags = hashMapProcessoTag.get(numeroTag);
            if(tipoAcao == TagImport.TIPO_INCLUIR){
                tags.forEach(o ->{
                    o.setOrgaoJulgador(processo.getOrgaoJulgador());
                    Tag tagNew = tagService.verificarSeExisteESalvar(o);
                    ProcessoTag processoTagNew = new ProcessoTag(tagNew,false, processo);
                    save(processoTagNew);
                    processo.getTags().add(processoTagNew);
                });
                localizadorListener.send(processo.getTags());
            }else if (tipoAcao == TagImport.TIPO_EXCLUIR){
                tags.forEach(o ->{
                    var tags1 =tagRepository.findByTagAndOrgaoJulgadorNotNull(o.getTag(), processo.getOrgaoJulgador());
                    if(!tags1.isEmpty()){
                        repository.deleteUpdate(processo,tags1.get(0),usuarioLogado);
                        processo.getTags().removeIf(it -> it.getTag().getId().equals(tags1.get(0).getId()));
                    }
                });
                localizadorListener.send(processo.getTags());
            }
        }
    }


    private HashMap<String, List<Tag>> mapProcessoTagImport(List<TagImport> tags){
        HashMap<String, List<Tag>> hashMapProcessoTag = new HashMap<>();

        tags.forEach(tag -> {
            List<Tag> listaTag = tag.getLisNomeTagSplit()
                .stream()
                .map(nomeTag -> new Tag(nomeTag, tag.getCor(), tag.getUrl(), tag.getAviso(), tag.getApenasMeuGabinete()))
                .collect(Collectors.toList());

            hashMapProcessoTag.computeIfAbsent(retornNrProcessoOuOab(tag), key -> new ArrayList<>()).addAll(listaTag);
        });
        return hashMapProcessoTag;
    }

    private String retornNrProcessoOuOab(TagImport tag) {
        if(tag.getSiglaEstado()!=null){
            return tag.getSiglaEstado()+tag.getNrOab();
        }else{
           return tag.getNrProcesso();
        }
    }

    private InputStreamResource criarArquivoTxt(List<String> list) throws IOException {
        BufferedWriter writer =null;
        File txtFile = File.createTempFile("processosNotFound", ".txt");

        try{
            writer = new BufferedWriter(new FileWriter(txtFile, StandardCharsets.ISO_8859_1));
            writer.write("OAB\n");
            for(String numeroProcesso: list){
                writer.write(numeroProcesso+"\n");
            }
        }finally {
            writer.close();
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(java.nio.file.Files.readAllBytes(txtFile.toPath()));
        return new InputStreamResource(inputStream);
    }

    private List<InputStreamResource>  criarArquivosCsv(List<TagImport>  list) throws IOException {
        List<InputStreamResource> resources = new ArrayList<>();

        List<TagImport> listaComNrProcesso = new ArrayList<>();
        List<TagImport> listaComNrOab = new ArrayList<>();

        for (TagImport tag : list) {
            if (tag.getNrProcesso() != null) {
                listaComNrProcesso.add(tag);
            }
            if (tag.getNrOab() != null) {
                listaComNrOab.add(tag);
            }
        }
        resources.add(criarArquivoCsv(listaComNrProcesso,ImportaTagCsv.CABECALHO_NUMERO_PROCESSO,"Processso"));
        resources.add(criarArquivoCsv(listaComNrOab,ImportaTagCsv.CABECALHO_NUMERO_OAB,"Oab"));

        return resources;
    }

    private InputStreamResource  criarArquivoCsv(List<TagImport> list,String cabecalho,String nome) throws IOException {
        BufferedWriter writer =null;
        File txtFile = File.createTempFile("todaBaseTagImport"+nome, ".csv");

        try{
            writer = new BufferedWriter(new FileWriter(txtFile, StandardCharsets.ISO_8859_1));
            writer.write(cabecalho+"\n");
            for(TagImport tagImport: list){
                writer.write(tagImport.toString()+"\n");
            }
        }finally {
            writer.close();
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(java.nio.file.Files.readAllBytes(txtFile.toPath()));

        return new InputStreamResource(inputStream);
    }

    public InputStreamResource buscaTodaTagImport() throws IOException {
        List<TagImport> tagImports = tagImportService.findAll();
        List<InputStreamResource> resources = criarArquivosCsv(tagImports);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        try{
            for (int i = 0; i < resources.size(); i++) {
                String fileName = "tag-geral" + i + ".csv";
                ZipEntry zipEntry = new ZipEntry(fileName);
                zipOutputStream.putNextEntry(zipEntry);
                StreamUtils.copy(resources.get(i).getInputStream(), zipOutputStream);
                zipOutputStream.closeEntry();
            }
        } finally {
            zipOutputStream.close();
        }

        byte[] zipBytes = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zipBytes);
        InputStreamResource resource = new InputStreamResource(byteArrayInputStream);

        return resource;
    }
}


