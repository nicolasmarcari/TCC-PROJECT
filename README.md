# TCC-PROJECT

Projeto prático do TCC 8ºsemestre de Ciências da Computação

## Guia para rodar o projeto via docker compose

Inicialmente vá para a tcc-api-backend/carbon e rode o seguinte comando:
```bash
./mvnw clean package

```
Ao rodar esse comando vai ser gerado a pasta target da aplicação JAVA, onde contem os binarios que vai ser recuperado pelo container.

Após isso basta voltar para a raiz do projeto e rodar

```bash
$ docker compose build

// espere rodar o build...

docker compose up -d

```

Para validar o funcionamento dos container basta executar o comando:

```bash
$ docker ps
```

Caso queira rodar o peojeto sem docker, observe o arquivo 'README.md' de cada projeto.

## Projeto

O projeto utiliza uma arquitetura em micro-serviços para fazer o cálculo da pegada de carbono baseado no tipo de combustivel, distância percorrida e eficiência do veículo.

- FRONTEND
-- front-end do projeto, utiliza um servidor NGINX e envia requisições para a API-BACKEND.

- TCC-API-BACKEND
-- api escrita em Java com Spring-boot responsável por realizar o cálculo da pegada e atribuir um UUID para enviar a TCC-API-DATABASE.

- TCC-API-DATABASE
-- api escrita em NodeJS com TypeScript responsável por validar dados, token e por fim salvar/recuperar dados do banco MongoDB.

**Documentações individuais podem ser encontradas na pasta de cada projeto**
