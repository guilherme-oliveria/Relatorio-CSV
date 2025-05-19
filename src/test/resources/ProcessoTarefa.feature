# language: pt
@ProcessoTarefa
Funcionalidade: Testar coletagem de métricas de Processos e Tarefas
  O sistema deve prover indicadores de Processos por tarefa e órgão julgador

  Cenario: Testar coleta de métricas
    Dado as seguintes quantidades de processos por tarefa e órgão julgador
      | Minutar     | 1164 | 34 |
      | NaoConcluso | 1164 | 35 |
      | Assinar	    | 1164 | 4  |
      | Minutar	    | 1166 | 412|
      | NaoConcluso | 1166 | 55 |
      | Assinar     | 1166 | 43 |
    Quando eu criar as tags para o counter
    Entao meu counter deverá ser
      |  34 | tarefa, Minutar     | orgao_julgador, 1164 |
      |  35 | tarefa, NaoConcluso | orgao_julgador, 1164 |
      |   4 | tarefa, Assinar     | orgao_julgador, 1164 |
      | 412 | tarefa, Minutar     | orgao_julgador, 1166 |
      |  55 | tarefa, NaoConcluso | orgao_julgador, 1166 |
      |  43 | tarefa, Assinar     | orgao_julgador, 1166 |
