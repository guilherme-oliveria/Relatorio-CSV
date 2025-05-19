[![pipeline status](http://git.tjro.jus.br/dides-jud/gabinete/back/badges/master/pipeline.svg)](http://git.tjro.jus.br/dides-jud/gabinete/back/commits/master)

[![coverage report](http://git.tjro.jus.br/dides-jud/gabinete/back/badges/master/coverage.svg)](http://git.tjro.jus.br/dides-jud/gabinete/back/commits/master) Master 

[![coverage report dev](http://git.tjro.jus.br/dides-jud/gabinete/back/badges/dev/coverage.svg)](http://git.tjro.jus.br/dides-jud/gabinete/back/commits/dev) Dev 

[![Quality Gate](https://sonar.tjro.jus.br/api/badges/gate?key=br.jus.tjro:gabinete)](https://sonar.tjro.jus.br/dashboard/index/br.jus.tjro:gabinete)
[![Sonar](https://sonar.tjro.jus.br/api/badges/measure?key=br.jus.tjro:gabinete&metric=lines)](https://sonar.tjro.jus.br/dashboard/index/br.jus.tjro:gabinete)
[![Sonar](https://sonar.tjro.jus.br/api/badges/measure?key=br.jus.tjro:gabinete&metric=function_complexity)](https://sonar.tjro.jus.br/dashboard/index/br.jus.tjro:gabinete)
[![Sonar](https://sonar.tjro.jus.br/api/badges/measure?key=br.jus.tjro:gabinete&metric=coverage)](https://sonar.tjro.jus.br/dashboard/index/br.jus.tjro:gabinete)
[![Sonar](https://sonar.tjro.jus.br/api/badges/measure?key=br.jus.tjro:gabinete&metric=new_coverage)](https://sonar.tjro.jus.br/dashboard/index/br.jus.tjro:gabinete)
[![Production](http://srvweb5.tjro.net:4000/production/gabinete-back.tjro.jus.br/version.json)](https://gabinete-back.tjro.jus.br/version.json)

# Módulo Gabinete

## Plugins

* [Editorconfig para o Eclipse](https://github.com/ncjones/editorconfig-eclipse#readme)

* [JaCoCo](https://rants.broonix.ca/java-code-coverage-with-gradle-and-jacoco-2/) - análise da cbertura de testes `gradle test jacocoTestReport` 

## Rodando o projeto manualmente

em **`src/main/resources/`**
crie um arquivo chamado **`application.properties`**
tendo como base o arquivo `application-sample.properties`
e ajuste o acesso ao banco de dados (usuário e senha)


Execute os comandos abaixo em um terminal (bash/prompt) *dentro da pasta deste projeto*.

Instale as dependências e rode o projeto   

    $ gradle bootRun

Ou gere o jar

    $ gradle assemble

E rode manualmente o jar do projeto

    $ java -jar build/libs/*.jar



    $ docker run -p 5432:5432 -e POSTGRES_PASSWORD=saiF0ra -e POSTGRES_USER=windson postgres

Para criar as entidades basta executar o liquidbase com o comando abaixo
    $ ./gradlew update

Caso adicione o contexto "contexts" o liquidbase vai criar varios registro de mock
    $ ./gradlew update -Pcontexts=developer

Para carregar um aplication properties diferente do padrao basta adicionar o parametro "prop"
    $ ./gradlew update -Pcontexts=developer -Pprop=sample


Acesse: `http://localhost:8090/`


## Docker

Empacote e crie o jar primeiro

    $ gradle clean assemble


Geração do container

    $ docker-compose build


Execução do projeto, exibindo a saída no console

    $ docker-compose up


Execução do projeto, sem exibir a saída no console

    $ docker-compose up -d


Execução do projeto, com outro aqruivo do docker compose

    $ docker-compose -f docker-compose-other.yml up  


## Publicação de uma nova versão


Informe o container e a versão

    $ docker build -t registro.tjro.jus.br/gabinete:1.x.x -t registro.tjro.jus.br/gabinete:latest .


Publique

    $ docker push registro.tjro.jus.br/gabinete:1.x.x && docker push registro.tjro.jus.br/gabinete:latest
