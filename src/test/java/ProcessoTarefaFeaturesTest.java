import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.infrastructure.metrics.ProcessoPorTarefa;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.Pt;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Cucumber.class)
@CucumberOptions(
    features={"src/test/resources"}
)
public class ProcessoTarefaFeaturesTest implements Pt {
    private List<String> tagsQuantidades;
    private List<ProcessoPorTarefa> processosPorTarefa;

    public ProcessoTarefaFeaturesTest() {
        Dado("^as seguintes quantidades de processos por tarefa e órgão julgador$", (DataTable table) -> this.processosPorTarefa = table.asLists(String.class).stream()
            .map(t -> new ProcessoPorTarefa(TarefaEnum.fromString(t.get(0)), Long.parseLong(t.get(1)), Long.parseLong(t.get(2))))
            .collect(Collectors.toList()));

        Quando("^eu criar as tags para o counter$", () -> this.tagsQuantidades =this.processosPorTarefa.stream()
            .map(p -> String.valueOf(p.getQuantidade()).concat(", " + String.join(", ", p.tags())))
            .collect(Collectors.toList()));

        Entao("^meu counter deverá ser$", (DataTable table) -> {
            final String[] expectedList = table.asLists().stream()
                .map(t -> String.join(", ", t))
                .toArray(String[]::new);

            assertThat(tagsQuantidades).containsExactly(expectedList);
        });
    }
}
