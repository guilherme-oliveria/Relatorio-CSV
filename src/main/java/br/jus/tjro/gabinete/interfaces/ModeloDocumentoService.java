package br.jus.tjro.gabinete.interfaces;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.BuscaModelo;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.ModeloDocumentoContador;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.ModeloMinutaContador;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.agrupador.AgrupadorModeloEnum;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.ModeloMinuta;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.transiente.ModeloMinutaCabecalhoRodape;
import br.jus.tjro.gabinete.model.gab.transiente.VariavelTemplate.VariavelTemplate;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ModeloDocumentoService {
    Page<ModeloMinuta> filterPaginated(Pageable pageable, AgrupadorModeloEnum agrupadorEnum, BuscaModelo buscaModelo, Usuario usuario) throws ServicoRemotoException;
    ModeloMinuta copiarParaUsuario(String id, String idOrgaoJulgador, Usuario usuario) throws Exception;
    ModeloMinutaContador totais(BuscaModelo buscaModelo, Usuario usuario) throws Exception;
    List<ModeloDocumentoContador> totaisDocumento(BuscaModelo buscaModelo, Usuario usuario) throws Exception;
    String renderizarVariavel(Usuario usuario, Processo processo, String minutaHtml, Boolean assinando) throws ServicoRemotoException;
    String renderizarVariavel(Usuario usuario, Processo processo, String template) throws ServicoRemotoException;
    ModeloMinutaCabecalhoRodape getCabecalhoERodape();
    List<VariavelTemplate> getVariaveisTemplate(String token) throws ServicoRemotoException;
    Optional<ModeloMinuta> findById(String id, Usuario usuario) throws Exception;
}
