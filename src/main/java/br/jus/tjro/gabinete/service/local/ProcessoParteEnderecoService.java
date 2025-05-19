package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.model.gab.endereco.Endereco;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParteEndereco;
import br.jus.tjro.gabinete.model.gab.transiente.ParteEnderecoTransiente;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoParteEnderecoRepository;
import br.jus.tjro.gabinete.service.remoto.ParteRemotoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ProcessoParteEnderecoService {
    
    private final Logger looger = LoggerFactory.getLogger(ProcessoParteEnderecoService.class);

    private final ProcessoParteEnderecoRepository processoParteEnderecoRepository;

    private final EnderecoService enderecoService;

    private final ParteRemotoService parteRemotoService;

    @Autowired
    public ProcessoParteEnderecoService(ParteRemotoService parteRemotoService,
                                        ProcessoParteEnderecoRepository processoParteEnderecoRepository,
                                        EnderecoService enderecoService){
        this.parteRemotoService = parteRemotoService;
        this.processoParteEnderecoRepository = processoParteEnderecoRepository;
        this.enderecoService = enderecoService;
    }

    public ProcessoParteEndereco salvaEnderecoDaParte(ParteEnderecoTransiente parteEnderecoTransiente, ProcessoParte processoParte) {

        Endereco endereco = enderecoService.busca(parteEnderecoTransiente.getEndereco(), processoParte.getProcesso().getSistema());

        ProcessoParteEndereco processoParteEndereco = new ProcessoParteEndereco(processoParte, endereco,
            processoParte.getIdParteLegado(), parteEnderecoTransiente.getEndereco().getId());

        try {
            return processoParteEnderecoRepository.save(processoParteEndereco);
        } catch (Exception e) {
            looger.error(e.getMessage(),e);
        }
        return processoParteEndereco;
    }

    public ProcessoParteEndereco importaOuAtualizaEnderecoDaParteDoProcesso(ProcessoParte processoParte) throws Exception {

        ParteEnderecoTransiente enderecoRemotoDaParte =
            parteRemotoService.pegarEnderecoDaParte(processoParte.getIdParteLegado(),
                processoParte.getProcesso().getSistema());

        if (enderecoRemotoDaParte != null) {
            //TODO mudar filtraProcessoParteExpediente de endereço para considerar a parte, pois quando é idParteLegado vai retornar endereços que nao tem relação
            List<ProcessoParteEndereco> enderecosLocaisDaParte =
                processoParteEnderecoRepository.findAllByIdEnderecoLegadoAndProcessoParte(enderecoRemotoDaParte.getEndereco().getId(),processoParte);
            enderecoRemotoDaParte = verificarQuaisEnderecosSalvar(enderecosLocaisDaParte,enderecoRemotoDaParte);
            if(enderecoRemotoDaParte != null)
                return salvaEnderecoDaParte(enderecoRemotoDaParte, processoParte);
        }
        return null;
    }

    public ParteEnderecoTransiente verificarQuaisEnderecosSalvar(List<ProcessoParteEndereco> enderecosLocaisDaParte,
                                                                 ParteEnderecoTransiente enderecoRemotoDaParte){
        Set<Long> idsDosEnderecosLegadosLocais = enderecosLocaisDaParte.stream()
            .map(ProcessoParteEndereco::getIdEnderecoLegado).collect(Collectors.toSet());

        if (enderecosLocaisDaParte.size() == 0 ||
            !idsDosEnderecosLegadosLocais.contains(enderecoRemotoDaParte.getEndereco().getId()))
            return enderecoRemotoDaParte;
        else
            return null;
    }
}
