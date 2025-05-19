# 9.1.4 (12-02-2020)
### Funcionalidades
- Agora o conteúdo dos modelos de documento é considerado como parâmetro para busca
- O módulo é capaz de enviar alertas no telegram quando um processo é concluso.

# 7.1.0 (13-09-2019)
### Funcionalidades
- Adição de procedimento para melhorar o carregamento das lotações do PJE

# 7.0.0 (09-09-2019)
### Funcionalidades
- As lotações agora são carregadas diretamente da base do Pje
- Os tipos de documento são carregados diretamente do Pje
- Quando a TPU estiver indisponível o sistema será capaz de continuar funcionando.

# 6.0.0 (05-08-2019)
### Correções (1 mudanças)
- Melhorias na importação dos autos do processo. Agora um processo pode estar disponivel para minuta antes do gabinete terminar de fazer download do pje.

# 5.0.0 (01-08-2019)
### Correções (1 mudanças)
- Realizado uma correção ao enviar a minuta ao PJE, que eventuamente removia a referencia do documento juntado no PJE

# 4.X.X (25-07-2019)
### Funcionalidades
- Modificado a forma que o módulo manipula os arquivos (documentos do processo, anexos das minutas e assinaturas de documentos)
- Ao abrir uma minuta, o módulo consulta a tarefa do processo no PJE; Caso o processo não esteja concluso uma tela para remover o processo do gabinete será aberta e o usuário devera decidir se mantém ou remove o processo no módulo
- Caso ocorra algum problema com a assinatura do magistrado (validação, armazenamento do documento, etc), o processo será enviado ser assinatura novamente.
### Correções
- Realizado diversas melhorias para enviar minutas assinadas ao Pje
- Correção ao salvar tags
- Pequenas melhorias ao receber um processo concluso do Pje
- Realizado uma correção que removia o movimento da minuta antes de enviar ao Pje, fazendo com que o processo não *CONTINUAR*
- Correção em localizadores que não funcionam quando a regra tem mais uma tag atribuida
- Correção ao excluir tag ao removê-la dos processos relacionados
- Realizado uma correção nas miniaturas dos autos do processo que afetava a interface do gabinete
### Outros
- Foi realizado uma redução no processamento dos localizadores para melhorar a performace do gabinete na integração com o PJe; A funcionalidade será otimizada no futuro.

# 3.2.0 (08-04-2019)
### Correções (3 mudanças)
- Melhoria no processamento de mensagens de localizador
- Melhoria na entrega de manifestação processual
- Melhor validação da minuta antes da assinatura de documento evitando erros na entrega de manifestação processual.

# 3.1.0 (14-01-2019)
### Funcionalidades (7 mudanças)
- Adição de um botão em detalhe de processo para atualizar o processo manualmente, ajudando na resolução de problemas relacionados à dados ausentes.
- Agora é possivel buscar processos que não estão no módulo do gabinete. 
- A exibição de notificações foi melhorada.
- Quando um erro ocorrer no sistema, um numero de identificação será exibido, sendo possível o rastreio completo do evento problemático.
- É possivel adicionar tag em lote ao processo.
- Os processos podem ser movidos entre as diferentes caixas.
- Agora é possivel visualizar a parte diretamente na lista de processos.
### Correções (2 mudanças)
- Em visualização de processos, foi reduzida a quantidade de caracteres que são exibidos para melhor adequação ao layout.
- Melhoria na validação da minuta, prevenindo o salvamento da minuta sem movimento, em casos esporádicos, e evitando falhas da integração com o pje

# 3.0.0 (01-10-2018)
### Funcionalidades (6 mudanças)
- Ganho de performance ao processar localizadores quando um processo é modificado.
- Quando o serviço de tabelas processuais estiver indisponivel, o sistema funcionará com algumas limitações porém não ficará indisponivel.
- A minuta pode ser enviada para correção diretamente na tela de assinatura em lote.
- Adicionada uma funcionalidade para detecção de erros de integração com o pje e o gabinete.
- Uma nova tela foi adicionada, nessa tela é possivel exibir os processos que estão com problemas de integração ao pje após a assinatura.
- Ao minutar um processo sigiloso a opção de publicação no Dje virá por padrão desativada.

### Correções (5 mudanças)
- Será exibida a minuta corretamente nas tarefas do pje após manifestação elaborada no gabinete. Essa correção afeta as 
atividades do cartório ao analisar processo recebido do gabinete.
- Melhorias para salvar arquivos no gabinete.
- Foi corrigido um problema que fazia com que as variáveis retornassem resultados inesperados, por exemplo: Fazendo com que endereço das partes apareceia quando não foi solicitado.
- Correção ao exibir imagens no inteiro teor.
- Correção na posição de documentos no inteiro teor.


## 2.1.0 (13-09-2018)
### Funcionalidades (4 mudanças)
- Agora é possível qualquer usuário excluir localizadores do gabinete
- Agora é possível criar localizadores com o mesmo nome
- O botão para selecionar diversas partes para um complemento de movimento foi removido, evitando o problema de se lançar 
diversos movimentos para o mesmo processo
- Foi adicionado diversas melhorias no monitoramento da aplicação como fila de importação de processos concluso, 
processos inconsistentes, processos pendentes de atualização e processos pendentes para devolução a origem.

### Correções (10 mudanças)
- Correção ao fazer solicitação de recomendações de movimento.
- Quando uma parte tem o nome retificado os localizadores serão atualizados de acordo
- Correção ao atribuir segredo de justiça a um processo
- Modificado o botão no modal que causava confusão com a funcionalidade de fechar
- Corrigido problema que ocorria ao realizar busca rápida de processo quando o componente já estava carregado
- Agora é possível realizar o download de anexos na assinatura em lote
- Documentos favoritos com o mesmo nome são exibidos agrupados
- Os documentos favoritos são ordenados pela data de juntada
- Documentos que foram juntados na horizontal não serão mais cortados ao gerar o interoteor
- Correção para abrir pdf que foi juntado com senha

## 2.0.6 (03-09-2018)
### Correções (1 mudança)
- Correção ao processar localizador quanto a tarefa do processo muda.

## 2.0.1 (29-08-2018)
### Correções (2 mudança)
- Correção na renderização das variáveis dos modelos de documentos
- Correção na exibição do nome das partes dos processos que estão sob segredo de justiça

## 2.0.X (27-08-2018)
### Funcionalidades (14 mudanças)

- Agora é possivel adicionar ou remover o segredo de justiça no processo
- É possivel atribuir segredo em anexos da manifestação
- Ao clicar no icone de uma pasta é possivel exibir os localizadores alinhados à essa regra sem carregar a 
lista de processo da referida pasta
- Foi adicionado a opção de gerenciar tags no menu de administração, sendo possivel a edição e exclusão
- O inteiro teor exibe a pagina dos documentos.
- Ícone de manifestações e localizadores agora são diferentes
- Melhorias no processamento de localizadores
- Alterada a cor padrão da fonte da minuta para preto
- Na assinatura em lote foi alterada a posição do movimento selecionado na validação
- Adicionado último usuário que editou a minuta na assinatura em lote
- Agora é possivel realizar a leitura de um processo que não esta concluso no gabinete. Porem ainda não é possivel 
buscar um processo que nunca foi incluido no gabinete
- Ao cadastrar localizador, foi alterado nome da aba de 'Aplicar Regras' para 'Adicionar Regras'
- As variáveis de processo agora aceitam parametros de configuração, como quebra de linha, por exemplo
- O gabinete vai atribuir tags automaticamente ao processo referente a meta do CNJ que ele se encontra

### Correções (4 mudanças)

- Apos assinar a manifestação as tags não permanentes serão removidas do processo
- Foi corrigido um erro na importação, quando a parte do processo existia no gabinete e era removida no sistema de origem
e esta mudança não refletia no gabinete.
- Correção na altura entre linhas do editor
- Adicionado verificação ortográfica (Sublinha palavras com erro ortográfico)

### Observações

- Foi enviado um alerta dia 14/08/2018 sobre uma lentidão relacionada com a assinatura dos processos. Nessa versão uma 
nova tecnologia foi empregada no processamento dos localizadores, porem a causa da lentidão não foi resolvida. Estaremos
coletando metricas do comportamento dessa nova tecnologia. Assim que concluirmos a analise das metricas vamos adotar esta
tecnologia para corrigir o problema da lentidão. A informação sobre a correção deve aparecer na proximo atualização. 


## 1.10.X (13-08-2018)
### Funcionalidades (3 mudanças)

- É possivel atribuir tags diretamente na visualização do processo
- Melhorias na experiência do usuário ao gerenciar os localizadores
- A inserção de anexos ao elaborar uma minuta foi melhorada

### Correções (4 mudanças)

- Melhoria na importação dos processos
- Correção ao processar localizador quando tag é atribuída
- Foram feitas diversas melhorias na integração do gabinete com o sistema legado
- Agora não é possivel adicionar um anexo sem informar o tipo do arquivo


## 1.9.4 (31-07-2018)
### Funcionalidades (1 mudanças)
- Adição de tags aos processos

## 1.9.X (25-07-2018)
### Funcionalidades (7 mudanças)

- É possivel acessar os documentos do processo na tela de minuta em lote;
- Adição de Data distribuição na lista de processo
- Favorita um documento ao clicar na estrela da tela "lista de documentos"
- Melhorias visuais na seleção de movimento
- Adicinada funcionalidades de edição e exclusão no Auto texto
- Na lista de lotações é possivel realizar buscas para selecionar um orgão julgador
- Ao finalizar a assinatura em lote o sistema é redirecionado a lista de processos e a lista é atualizada corretamente

### Correções (3 mudanças)

- Corrigido problema que muda os processos de assinando para minutando sem interação do usuario
- Agora a lista de favoritos exibe a descrição que informada pelo usuario
- Correção ao carregar modelos de documento ao minutar em lote

## 1.8.X (09-07-2018)
### Funcionalidades (6 mudanças)

- Última data de sincronização
- Adição de documentos de identificação da parte do processo
- Melhoria na seleção de movimento processual
- Adição de suporte à imagens nos documentos processuais
- Mudança na posição das notificações da aplicação
- No detalhe do processo, adição de botão voltar à lista de processo

### Correções (6 mudanças)

- Correção na importação de documentos das partes
- Correção ao gerar intero teor quando o html tem elementos de entrada de dados
- Opção de download de formatos não compativeis de documentos processuais
- Correção na pesquisa de processos que comecem com zero
- Correção na navegação da assinatura em lote
- Ajuste na posição dos icones de prioridade

## 1.7.X (25-06-2018)
### Funcionalidades (4 mudanças)

- Funcionalidade para copiar modelo de documentos de outros órgãos
- Adição da funcionalidade para parágrafo no editor de documentos
- Adição de monitoramento das dependências do sistema
- Assinador 1.3 foi atualizado à versão estável

### Correções (10 mudanças)

- Correção na importação de documentos do processo do sistema legado
- Correção na importação de documentos do processo, nenhum arquivo é gravado no diretório temporário
- Correção na ordenação da lista de documentos
- Correção na exibição de páginas de documento PDF
- Documentos html não são mais afetados pelo estilo da página
- Correção na importação dos endereços da parte
- Correção na devolução à origem sem manifestação
- Correção na exibição de páginas de documento
- Correção na ordenação dos documentos ao gerar inteiro teor
- Tratamento para arquivos que não são PDF ao gerar o inteiro teor

## 1.6.X (29-05-2018)
### Funcionalidades (5 mudanças)

- Melhoria no classificador de documentos, agora mostra 3 opções em vez de uma
- Durante a elaboração da minuta no editor ao precionar as teclas "ctrl + espaco" uma caixa com recomendações será sugerida
- Novo motor de processamento dos localizadores
- Quando um documento for assinado esse será enviado para a inteligencia artificial aprender por reforço
- A lista de movimentos processuais carrega conforme o usuario utiliza o scroll do mouse

### Correções (6 mudanças)

- Melhoria nas mensagens de erro quando micro servidores estiverem indisponiveis
- Correção na assinatura quando continha um caracter especial de aspa
- Correção nos icones de prioridades processuais
- Correção no redirecionamento quando era realizado uma assinatura em lote
- Correção no rediricionamento quando o assinador estava indisponivel no computador do cliente
- Correção no tipo de documento quando devolvia o processo ao sistema legado

## 1.5.X (14-05-2018)
### Funcionalidades (3 mudanças)

- Recomendação de movimentos de uma minuta (Inteligência Artificial);
- Redução do texto visualizado da publicação DJe na tela de assinatura em lote, com possibilidadde de expansão;
- Melhorias no layout da tela de assinatura em lote;

### Correções (5 mudanças)

- Correção na lógica em outras opções da minuta;
- Correção no redirecionamento após assinatura;
- Correção no redirecionamento ao sair do sistema;
- Mensagem de erro quando ocorrer um problema na geração do inteiro teor;
- Mensagem de erro quando ocorrer erro de validação da assinatura;


## 1.4.X (02-05-2018)
### Funcionalidades (4 mudanças)

- Alterado a página inicial do Gabinete;
- Liberado a todos os usuários do orgão julgador poder editar modelos de minuta que pertencem ao orgão julgador;
- Adicionado à variável 'Advogado', Procuradoria e Defensoria;
- A lista de modelos de minuta padrão agora é 'Modelos da Vara';
- Adicionado o ID do documento no inteiro teor;

### Correções (5 mudanças)

- Correções no Sidebar;
- O inteiro teor ordena os documentos corretamente;
- A assinatura do inteiro teor não sobrescreve mais o texto original
- Correção na regra 'Assunto' dos localizadores;
- Ao salvar um localizador, agora é informado se houver uma regra pendente de adição;
- Correção no redirecionamento após expirar a autênticação;
- Correção no charset ao integrar uma minuta com o PJe;


## 1.3.0 (20-04-2018)

### Funcionalidades (5 mudanças)

- Foi liberada a criação de localizador para usuários além dos magistrados
- Ao criar um localizador, as manifestações são ordenadas por ordem alfabética
- Adicionada função para verificar versão da aplicação na url "/" 
- Adicionada a funcionalidade que impede o HTML de ser selecionado no modal de modelos de documento na tela da minuta
- Os localizadores são agrupados por manifestação, caso eles possuam uma regra para tal

### Correções (1 mudança)

- Foi corrigido um erro na assinatura que ocorria na renderização de modelos de documento


## 1.2.1 (04-04-2018)

### Funcionalidades (5 mudanças)

- Agora é possível devolver o processo sem assinar nenhuma manifestação. Para realizar a operação basta clicar no botão 
"Devolver sem Manifestação" na tela de visualização do processo;
- Foi adicionado diversas opções no PJe para enviar o processo concluso ao módulo do gabinete.
Exemplos: Julgamento Homologação, Julgamento Revelia, Julgamento Mérito, etc;
- Foi adicionado a opção de download de documentos na tela de visualização rápida;
- O leitor de documentos carrega todas as páginas do documento;
- O acesso aos documentos favoritos agora é realizado por uma barra que é exibida à esquerda.

### Correções (4 mudanças)

- A variável que contêm o nome do orgão julgador na criação de um novo modelo foi corrigida;
- A contagem de processos que estão no localizador foi corrigida;
- Corrigido um problema que modificava a data de conclusão de processos que já estavam concluso.
- O inteiro teor agora é gerado com a extenção .pdf


## 1.1.0 (20-03-2018)

### Funcionalidades (11 mudanças)

- No inteiro teor foi adicionado informação da parte que realizou a manifestação;
- Em modelos foram adicionadas novas variáveis, nome da parte e endereço, nome do advogado e OAB;
- Foram unificadas as telas de visualização do processo fazendo com que a navegação seja mais intuitiva;
- Implementado um recurso que dispensa o carregamento pela segunda vez do processo;
- Implementado uma tela para assinatura em lote com validação rápida das minutas;
- Implementado um filtro de modelos, agora todos os modelos onde o usuário esta lotado serão exibidos por uma nova aba na lista de modelos;
- Agora ao navegar nos documentos do processo o número do PJe é exibido adequadamente;
- Os botões na tela da minuta foram reposicionados pois estavam sobrepondo outros botões;
- Ao utilizar modelo de documento em um processo com sigilo ou com menor, as partes serão impressas somente com iniciais;
- É possivel selecionar várias partes para um movimento de forma rápida;
- Agora é possível selecionar qualquer assunto da árvore para buscar processos.

### Correções (4 mudanças)

- Foi corrigido um erro ao salvar modelo;
- Foi corrigido o erro que ocorria ao renderizar modelos de documento;
- Foi corrigido um erro que ocorria ao gerar um inteiro teor;
- Suprimido correções na importação dos dados ao PJe.
