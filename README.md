GUIA PARA RODAR DOCKER COMPOSE

Inicialmente vá para a api-backend/carbon e rode o comando:
```bash
./mvnw clean package

```

Ao rodar esse comando vai ser gerado a pasta target da aplicação JAVA, onde contem os binarios que vai ser recuperado pelo container.

Após isso basta voltar para a raiz do projeto e rodar

```bash
$ docker compose build

// espere rodar o build dps

docker compose up -d

```

