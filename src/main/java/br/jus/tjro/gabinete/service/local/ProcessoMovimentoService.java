package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.dto.MovimentoProcessoDTO;
import br.jus.tjro.gabinete.model.gab.transiente.MovimentoProcesso;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoDocumento;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoMovimento;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoMovimentoRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.service.local.processo.documento.ProcessoDocumentoService;
import br.jus.tjro.gabinete.service.remoto.MovimentoRemotoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ProcessoMovimentoService {
    
    private final Logger looger = LoggerFactory.getLogger(ProcessoMovimentoService.class);

    protected ProcessoMovimentoRepository repository;

    protected MovimentoRemotoService movimentoRemotoService;

    @Autowired
    protected ProcessosRepository processosRepository;

    @Autowired
    protected MovimentoFavoritoService movimentoService;

    @Autowired
    protected ProcessoDocumentoService processoDocumentoService;

    @Autowired
    public ProcessoMovimentoService(ProcessoMovimentoRepository repository, MovimentoRemotoService movimentoRemotoService){
        this.repository = repository;
        this.movimentoRemotoService = movimentoRemotoService;
    }


    public Page<MovimentoProcessoDTO> pegaMovimentosDoProcessoPeloIdProcesso(Long idProcesso, Pageable pageable) throws Exception {

        Page<MovimentoProcessoDTO> movimentosLocais = repository.pegaMovimentosDoProcessoPeloIdProcesso(idProcesso, pageable);
        if (movimentosLocais.getTotalElements() > 0)
            return movimentosLocais;

        Processo processo = processosRepository.getById(idProcesso);
        try {
            importaOuAtualizaMovimentosDoProcesso(processo);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return repository.pegaMovimentosDoProcessoPeloIdProcesso(idProcesso, pageable);
    }

    @Transactional
    public List<ProcessoMovimento> importaOuAtualizaMovimentosDoProcesso(Processo processo) throws Exception {
        List<ProcessoMovimento> processoMovimentosLocais = repository.findByProcesso(processo);
        List<MovimentoProcesso> movimentoRemotosDoProcesso =
            movimentoRemotoService.pegaMovimentoProcesso(processo.getIdProcessoSistemaLegado(), processo.getFonteDados());

        Integer processoMovimentosLocaisCount = processoMovimentosLocais.size();
        Integer movimentoRemotosDoProcessoCount = movimentoRemotosDoProcesso.size();

        List<ProcessoMovimento> processoMovimentosAhImportar = new ArrayList<>();
        // se a quantidade de movimentos remotos for maior, então adiciona os novos no Gabinete
        if (!Objects.equals(movimentoRemotosDoProcessoCount, processoMovimentosLocaisCount)) {

            // se já tem movimentos do processo cadastrados no Gabinete, remove da lista os já cadastrados
            if (processoMovimentosLocaisCount != 0) {
                //pega os ids locais
                Set<Long> idsLocais = processoMovimentosLocais.stream()
                    .map((ProcessoMovimento::getIdProcessoMovimentoLegado)).collect(Collectors.toSet());
                //remove da lista remota os locais
                movimentoRemotosDoProcesso = movimentoRemotosDoProcesso.stream()
                    .filter(movimentoRemoto -> !idsLocais.contains(movimentoRemoto.getId()))
                    .collect(Collectors.toList());
            }

            // adicona os que sobraram ou todos os iniciais
            movimentoRemotosDoProcesso.forEach(procMov -> {
                try {
                    processoMovimentosAhImportar.add(
                        new ProcessoMovimento(processo, procMov.getCdEvento(),
                            relacionaMovimentoDocumento(procMov, processo), procMov.getId(), procMov.getData(),
                            procMov.getDescricao(), procMov.getIn_visibilidade_externa(), procMov.getIn_ativo(),
                            procMov.getId_orgao_julgador(), procMov.getId_orgao_julgador_colegiado(),
                            procMov.getNome_usuario(), procMov.getCpf_usuario())
                    );
                } catch (Exception e) {
                    looger.error("Ocorreu um erro ao tentar salvar o Movimento " +
                        procMov.getId() + " do Processo id" + processo.getId(),e);
                    
                }
            });
            try {
                return repository.saveAll(processoMovimentosAhImportar);
            } catch (Exception e) {
                looger.error(e.getMessage(),e);
            }
        }
        return processoMovimentosLocais;
    }

    private ProcessoDocumento relacionaMovimentoDocumento(
        MovimentoProcesso movimentoProcesso, Processo processo
    ) throws Exception {
        if (movimentoProcesso.getDocumento() != null)
            return processoDocumentoService.getDocumentoProcessoAndIdLegadoEhImportaOuAtualiza(
                movimentoProcesso.getDocumento().getId().toString(), processo
            );
        return null;
    }
}
