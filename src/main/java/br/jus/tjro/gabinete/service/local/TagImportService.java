package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.model.gab.tag.Tag;
import br.jus.tjro.gabinete.model.gab.tag.TagImport;
import br.jus.tjro.gabinete.repository.gab.TagImportRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagImportService {

    private TagImportRepository tagImportRepository;

    public TagImportService(TagImportRepository tagImportRepository) {
        this.tagImportRepository = tagImportRepository;
    }


    public TagImport verificarSeExisteESalvar(TagImport tagObj) {
        var tag = tagImportRepository.findByNrProcessoAndNomeTag(tagObj.getNrProcesso(), tagObj.getNomeTag());
        return tag.orElseGet(() -> save(tagObj));
    }

    public TagImport verificarSeExisteESalvarOab(TagImport tagObj) {
        var tag = tagImportRepository.findByNrOabAndSiglaEstadoAndNomeTag(tagObj.getNrOab(), tagObj.getSiglaEstado(),  tagObj.getNomeTag());
        return tag.orElseGet(() -> save(tagObj));
    }

    public TagImport save(TagImport tag)  {
        return tagImportRepository.save(tag);
    }

    public void deleteByNrProcessoAndNomeTag(TagImport tagObj){
        tagImportRepository.deleteByNrProcessoAndNomeTag(tagObj.getNrProcesso(), tagObj.getNomeTag());
    }

    public void deleteByNrOabAndSiglaEstadoAndNomeTag(TagImport tagObj){
        tagImportRepository.deleteByNrOabAndSiglaEstadoAndNomeTag(tagObj.getNrOab(), tagObj.getSiglaEstado(), tagObj.getNomeTag());
    }

    public List<TagImport> findByNrProcesso(String nrProcesso){
        return tagImportRepository.findByNrProcesso(nrProcesso);
    }

    public List<TagImport> findByNrOabAndSiglaEstado(String nrOab, String siglaEstado){
        return tagImportRepository.findByNrOabAndSiglaEstado(nrOab,siglaEstado);
    }

    public void deleteAll(){
        tagImportRepository.deleteAll();
    }

    public List<TagImport> findAll(){
        int pageSize = 999;
        int page = 0;
        List<TagImport> allTagImports = new ArrayList<>();

        while (true) {
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<TagImport> tagImportPage = tagImportRepository.findAll(pageable);
            if (!tagImportPage.hasContent()) {
                break;
            }
            allTagImports.addAll(tagImportPage.getContent());
            page++;
        }
        return allTagImports;
    }
}
