package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.interfaces.StorageService;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import br.jus.tjro.gabinete.repository.gab.minuta.MinutasAnexoRepository;
import br.jus.tjro.gabinete.util.Md5Util;
import br.jus.tjro.gabinete.util.PdfHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Strings.isNullOrEmpty;
@Service
public class MinutaAnexoService {

    @Autowired
    private MinutasAnexoRepository minutaAnexosRepository;

    @Autowired
    private StorageService storageService;

    public MinutaAnexo save(MinutaAnexo minutaAnexo) throws Exception {
        if (minutaAnexo.getMinutaPai() == null)
            throw new Exception("O id do documento pai não pode ser nullo");
        if(isNullOrEmpty(minutaAnexo.getHash())) {
            String hash = storageService.store(minutaAnexo.getHtml().getBytes());
            minutaAnexo.setHash(hash);
        }
        if(minutaAnexo.getPosicao() == null) {
            Integer posicao = minutaAnexosRepository.nextPosition(minutaAnexo.getIdMinutaPai());
            if(posicao != null)
                minutaAnexo.setPosicao(posicao+1);
            else
                minutaAnexo.setPosicao(1);
        }
        return minutaAnexosRepository.save(minutaAnexo);
    }

    public List<Long> reordenar(List<Long> idsAnexos) throws Exception {
        Integer ordem = 0;
        for (Long idAnexo : idsAnexos) {
            MinutaAnexo anexo = minutaAnexosRepository.findById(idAnexo).orElseThrow(NullPointerException::new); // ToDo tratar a falta desse recurso
            anexo.setPosicao(ordem);
            ordem++;
            minutaAnexosRepository.save(anexo);
        }
        return idsAnexos;
    }

    public MinutaAnexo save(Long idDocumento, MultipartFile file) throws Exception {
        String hash = storageService.store(file);
        if(hash == null || hash.isBlank()) {
            throw new Exception("Hash (" + hash + ") inválido ao registrar arquivo no storage");
        }

        MinutaAnexo minutaAnexo = new MinutaAnexo(new Minuta(idDocumento), hash, file.getContentType());
        return this.save(minutaAnexo);
    }

    public List<MinutaAnexo> anexosFromMinuta(Long idMinuta) throws Exception {
        return minutaAnexosRepository.findByMinutaPaiId(idMinuta);
    }

    public int atualizaSigilo(boolean sigilo, Long id) throws Exception {
        return this.minutaAnexosRepository.atualizaSigilo(sigilo, id);
    }

    private MinutaAnexo setContentType(MinutaAnexo minutaAnexo, String contentType) throws Exception {
        InputStream inputStream = storageService.loadInputStream(minutaAnexo.getHash());
        if (PdfHelper.verificaPdf(inputStream))
            minutaAnexo.setContentType("application/pdf");
        else
            minutaAnexo.setContentType(contentType);
        return minutaAnexo;
    }

    public MinutaAnexo findOne(Long idMinutaAnexo) throws Exception {
        MinutaAnexo minutaAnexo = minutaAnexosRepository.findById(idMinutaAnexo).orElseThrow(NullPointerException::new); // ToDo tratar a falta desse recurso
        if (minutaAnexo == null)
            throw new Exception("Não existe registro no banco de dados");
        return minutaAnexo;
    }

    public void delete(Long id) {
        // Verifica se não existe outro hash, se nao existir remove anexo do file system
        // tambem.
        Optional<MinutaAnexo> minutaAnexo = minutaAnexosRepository.findById(id);
        minutaAnexo.ifPresent(anexo -> {
            verificaPermiteExclusao(anexo);
            minutaAnexosRepository.deleteById(anexo.getId());
        });
    }

    private void verificaPermiteExclusao(MinutaAnexo minutaAnexo) {
        if(minutaAnexo.getTipoDocumento().isPreliminar()) {
            boolean isEmSessao = minutaAnexo.getMinutaPai().getProcesso().temTag("EM_SESSAO");
            if (isEmSessao) throw new RuntimeException("Não é possível excluir preliminar de minuta em sessão");
        }
    }

    public void deleteAllByMinuta(Minuta minuta) {
        minutaAnexosRepository.deleteAllById(minuta.getAnexos().stream().map(MinutaAnexo::getId).toList());
    }

    public InputStream loadInputStream(MinutaAnexo minutaAnexo) throws Exception {
        return storageService.loadInputStream(minutaAnexo.getHash());
    }

    public List<MinutaAnexo> getAnexosByminutaPai(Minuta minuta) {
        return minutaAnexosRepository.findByMinutaPai(minuta);
    }
}
