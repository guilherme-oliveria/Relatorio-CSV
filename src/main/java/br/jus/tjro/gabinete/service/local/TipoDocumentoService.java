package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.config.ColegiadoEnvironment;
import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.config.JuizLeigoEnvironment;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.repository.gab.TipoDocumentoRepository;
import br.jus.tjro.gabinete.repository.gab.minuta.MinutasRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class TipoDocumentoService {


    private final Logger log = LoggerFactory.getLogger(TipoDocumentoService.class);

    private final TipoDocumentoRepository repository;
    private final ColegiadoEnvironment colegiadoEnvironment;
    @Autowired
    private MinutasRepository minutasRepository;

    @Autowired
    private CaixaService caixaService;

    private final JuizLeigoEnvironment juizLeigoEnvironment;

    @Autowired
    public TipoDocumentoService(TipoDocumentoRepository repository, ColegiadoEnvironment colegiadoEnvironment,JuizLeigoEnvironment juizLeigoEnvironment) {
        this.repository = repository;
        this.colegiadoEnvironment = colegiadoEnvironment;
        this.juizLeigoEnvironment = juizLeigoEnvironment;
    }

    public List<TipoDocumento> pegaTodos() {
        return findByAtivoTrueAndMinutaFalseOrderByDescricaoAsc();
    }

    public List<TipoDocumento> pegaTodosInclusiveMinuta() {
        return findByAtivoTrueOrderByDescricaoAsc();
    }

    public List<TipoDocumento> pegaTodosMinuta() {
        return findByAtivoTrueAndMinutaTrueOrderByDescricaoAsc();
    }

    public Optional<TipoDocumento> findById(String id) {
        return repository.findById(id);
    }

    public List<TipoDocumento> findByAtivoTrueAndMinutaFalseOrderByDescricaoAsc() {
        log.info("findByAtivoTrueAndMinutaFalseOrderByDescricaoAsc");
        return repository.findAll().stream().filter(tpDoc -> tpDoc.isAtivo() && !tpDoc.isMinuta())
            .sorted().collect(Collectors.toList());
    }

    public List<TipoDocumento> findByAtivoTrueOrderByDescricaoAsc() {
        log.info("findByAtivoTrueOrderByDescricaoAsc");
        return repository.findAll().stream().filter(TipoDocumento::isAtivo).sorted().collect(Collectors.toList());
    }

    public List<TipoDocumento> findByAtivoTrueAndMinutaTrueOrderByDescricaoAsc() {
        log.info("findByAtivoTrueAndMinutaTrueOrderByDescricaoAsc");
        return repository.findAll().stream()
            .filter(tpDoc -> tpDoc.isAtivo() && (tpDoc.isMinuta() || juizLeigoEnvironment.isMinutaJuizLeigo(tpDoc.getId())))
            .sorted().collect(Collectors.toList());
    }

    public List<TipoDocumento> buscaTipoDocumentoMinuta(Long idMinuta) throws ServicoRemotoException {
        Optional<Minuta> minuta = minutasRepository.findById(idMinuta);
        List<TipoDocumento> tipoDocumentos = new ArrayList<>();
        if(minuta.isPresent()){
            if(minuta.get().getTiposDocumentos()!=null){
                return repository.findAll(minuta.get().getTiposDocumentos());
            }else{
                return findByAtivoTrueAndMinutaTrueOrderByDescricaoAsc();
            }
        }

        return tipoDocumentos;
    }

    public List<TipoDocumento> buscaTipoDocumentoCaixa(Integer idCaixa) throws ServicoRemotoException {
        Optional<Caixa> caixa = caixaService.findById(idCaixa);
        List<TipoDocumento> tipoDocumentos = new ArrayList<>();
        if(caixa.isPresent()){
            List<Minuta> minuta = minutasRepository.findTopMinutaTipoDocByCaixaIdOrderByDesc(idCaixa, PageRequest.of(0, 1));
            if(minuta !=null && minuta.size()>0){
                return repository.findAll(minuta.get(0).getTiposDocumentos());
            }else{
                return findByAtivoTrueAndMinutaTrueOrderByDescricaoAsc();
            }
        }
        return tipoDocumentos;
    }

}
