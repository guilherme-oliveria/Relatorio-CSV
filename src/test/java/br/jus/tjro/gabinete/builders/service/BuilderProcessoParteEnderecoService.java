package br.jus.tjro.gabinete.builders.service;

import br.com.six2six.fixturefactory.Fixture;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParteEndereco;
import br.jus.tjro.gabinete.model.gab.transiente.EnderecoTransiente;
import br.jus.tjro.gabinete.model.gab.transiente.ParteEnderecoTransiente;
import br.jus.tjro.gabinete.model.gab.transiente.Partes;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoParteEnderecoRepository;
import br.jus.tjro.gabinete.service.local.EnderecoService;
import br.jus.tjro.gabinete.service.local.ProcessoParteEnderecoService;
import br.jus.tjro.gabinete.service.remoto.ParteRemotoService;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class BuilderProcessoParteEnderecoService {

    public ProcessoParteEnderecoService get() throws Exception {

        ProcessoParteEnderecoService processoparteService = new ProcessoParteEnderecoService(
            getParteRemotoService(),
            getProcessoParteEnderecoRepository(),
            getEnderecoService());

        return processoparteService;
    }

    private EnderecoService getEnderecoService() {
        BuilderEnderecoService builder = new BuilderEnderecoService();
        EnderecoService service = builder.get();
        return service;
    }

    private ParteRemotoService getParteRemotoService() throws Exception {
        BuilderParteRemotoService builder = new BuilderParteRemotoService();
        ParteRemotoService service = builder.get();
        when(service.pegarEnderecoDaParte(any(Long.class),any(FonteDadosEnum.class))).thenAnswer(
            (Answer<ParteEnderecoTransiente>) invocation -> {
                Long id = invocation.getArgument(0);

                EnderecoTransiente endereco = new EnderecoTransiente();
                endereco.setId(22l);
                ParteEnderecoTransiente parteEnd = new ParteEnderecoTransiente();
                Partes parte = new Partes();

                parteEnd.setEndereco(endereco);
                parteEnd.setId(id);
                parteEnd.setParte(parte);
                return parteEnd;
            });
        return service;
    }

    private ProcessoParteEnderecoRepository getProcessoParteEnderecoRepository(){
        ProcessoParteEnderecoRepository repository = mock(ProcessoParteEnderecoRepository.class);

        List<ProcessoParteEndereco> listaNoBanco = getListaProcessoParteEndereco();

        when(repository.save(any(ProcessoParteEndereco.class))).thenAnswer(
            (Answer<ProcessoParteEndereco>) invocation -> {
                ProcessoParteEndereco ent = invocation.getArgument(0);
                if(ent.getId() == null)
                    ent.setId(new Random().nextLong());
                return ent;
            });

        when(repository.findAllByIdEnderecoLegadoAndProcessoParte(any(Long.class),any(ProcessoParte.class))).thenAnswer(
            (Answer<List<ProcessoParteEndereco>>) invocation -> {
                Long idLegado = invocation.getArgument(0);
                ProcessoParte processoParte = invocation.getArgument(1);
                return listaNoBanco.stream().filter(
                    p -> p.getIdEnderecoLegado() == idLegado &&
                        p.getIdProcessoParte() == processoParte.getId()).collect(Collectors.toList());
            });


        return repository;
    }

    private List<ProcessoParteEndereco> getListaProcessoParteEndereco() {
        List<ProcessoParteEndereco> banco = new ArrayList<>();
        ProcessoParteEndereco pjePg = new ProcessoParteEndereco();
        pjePg.setIdEnderecoLegado(6l);
        pjePg.setId(1l);
        pjePg.setIdProcessoParte(1l);
        pjePg.setIdEnderecoLegado(22l);
        banco.add(pjePg);
        return banco;
    }

    private ParteEnderecoTransiente getParteEnderecoTransiente(){
        return Fixture.from(ParteEnderecoTransiente.class).gimme("valido");
    }
}
