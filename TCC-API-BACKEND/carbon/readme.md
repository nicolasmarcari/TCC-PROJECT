# TCC-API-DATABASE

## Inicio

Para rodar a aplicação:

```bash
mvn spring-boot:run
# ou
mvnw.cmd spring-boot:run
```

## Dependências Utilizadas

- spring-boot-starter-web
- spring-boot-devtools
- springdoc-openapi-starter-webmvc-ui
- lombok
- spring-boot-starter-test
- jackson-databind

## Rotas

### `POST /sendCarbon`

#### Funcionalidade

- A rota é responsável por receber os dados do usuário, calcular a pegada de carbono e devolver não só o UUID, mas também a pegada de carbono respectiva.

#### Payload

```json
{
	"veiculo": "Nome do Veiculo",
	"tipo_combustivel": "Gasolina | Diesel",
	"distancia_percorrida": Number,
	"eficiencia": Number,
}

```

#### Retornos

**201 OK**

```json
{
  "uuid": "00000000-0000-0000-0000-000000000000",
  "pegada_de_carbono": Number
}
```

**400 Bad Request**

```json
{
  "timestamp": "2002-07-15T20:07:00.0230628",
  "status": 400,
  "error": "Validation Failed",
  "message": "Um ou mais campos estão inválidos",//exemplo de erro.
  "path": "/api/sendCarbon",
  "traceId": null,
  "details": [
    {
      "message": "O veículo deve ser preenchido.",//Exemplo de erro.
      "rejectedValue": "",
      "field": "vehicle"
    }
  ]
}
```

ou

```json
{
  "timestamp": "2002-07-15T20:07:00.2607474",
  "status": 400,
  "error": "Validation Failed",
  "message": "Requisição inválida",
  "path": "/api/sendCarbon",
  "traceId": null,
  "details": null
}
```
**401 Unauthorized**

```json
{
  "timestamp": "2002-07-15T20:07:00.637575",
  "status": 401,
  "error": "Downstream service error",
  "message": "Erro ao chamar serviço externo",
  "path": "/api/sendCarbon",
  "traceId": null,
  "details": [
    {
      "downstreamBody": ""
    }
  ]
}
```

**500 Internal Server Error**

```json
{
  "timestamp": "2002-07-15T20:07:00.2921876",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Ocorreu um erro interno. Tente novamente mais tarde.",
  "path": "/api/sendCarbon",
  "traceId": null,
  "details": [
    "Handler dispatch failed: java.lang.Error: Unresolved compilation problem: \n\tallData cannot be resolved to a variable\n"
  ]
}
```

### `GET /getVehicle/{uuid}`

#### Funcionalidade

Retorna o objeto do veiculo inteiro a partir do UUID que vai ser passado por parametro.

#### Parâmetros
````
http://url:port/UUID
````

#### Retornos

**200 OK**

```json
{
	"_id": ObjectId,
	"veiculo": "Nome do Veiculo",
	"tipo_combustivel": "Gasolina | Diesel",
	"distancia_percorrida": Number,
	"eficiencia": Number,
	"uuid": "UUID"
}
```

**401 Unauthorized**

```json
{
  "timestamp": "2002-07-15T20:07:00.3083366",
  "status": 401,
  "error": "Downstream service error",
  "message": "Erro ao chamar serviço externo",
  "path": "/api/getVehicle/00000000-0000-0000-0000-000000000000",
  "traceId": null,
  "details": [
    {
      "downstreamBody": "{\"message\":\"Unauthorized\"}"
    }
  ]
}
```


**404 Not Found**
```json
{
  "timestamp": "2002-07-15T20:07:00.4428564",
  "status": 404,
  "error": "Downstream service error",
  "message": "Erro ao chamar serviço externo",
  "path": "/api/getVehicle/00000000-0000-0000-0000-000000000000",
  "traceId": null,
  "details": [
    {
      "downstreamBody": "vehicle not found"
    }
  ]
}
```

**500 Internal Server Error**
```json
{
  "timestamp": "2002-07-15T20:07:00.7762289",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Ocorreu um erro interno. Tente novamente mais tarde.",
  "path": "/api/getVehicle/00000000-0000-0000-0000-000000000000",
  "traceId": null,
  "details": [
    "Handler dispatch failed: java.lang.Error: Unresolved compilation problems: \n\tresponse cannot be resolved"
  ]
}
```