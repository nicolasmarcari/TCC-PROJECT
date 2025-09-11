# TCC-API-DATABASE

## Inicio

Para rodar a aplicação:

```bash
npm i
npm run dev
```

Também é necessário colocar as variaveis de ambiente dentro do .env

```
PORT: 3000 // ou outra porta (opcional)
MONGO_URI=mongodb://localhost:27017 // ou sua url
SECRET_TOKEN='AWESOME_TOKEN' // seu token
```

## Dependências Externas

- MongoDB

## Rotas

### `POST /saveVehicle`

#### Funcionalidade

Rota responsável por salvar o veiculo junto com o nome do veiculo, quilimetragem percorrida, tipo de combustivel, uuid, eficiencia e sua pegada de carbono emitida.

A rota valida os tipos de dados recebidos e todos devem estar preenchidos.

#### Payload

```json
Header: Authorization TOKEN
{
	"veiculo": "String",
	"tipo_combustivel": "Gasolina | Diesel",
	"distancia_percorrida": Number,
	"eficiencia": Number,
	"pegada_de_carbono": Float,
	"uuid": "UUID"
}
```

#### Retornos

**201 OK**

```json
{
	"message": "Success! Data saved!"
}
```

**400 Bad Request**

```json
{
	"message": "Validation Error",
	"errors": {
		"_errors": [],
		"atributo": { // aqui vai o que esta faltando na request
			"_errors": [
				"Required"
			]
		}
	}
}
```

ou

```json
{
	"message": "Validation Error",
	"errors": {
		"_errors": [],
		"pegada_de_carbono": { // aqui vai o campo com erro
			"_errors": [
				"Expected <tipo esperado>, <tipo recebido>"
			]
		}
	}
}
```

**401 Unauthorized**

```json
{
	"message": "missing token"
}
```

ou

```json
{
    "message": "Unauthorized"
}
```

**500 Internal Server Error**

```json
{
    "message": "Internal Server Error"
}
```





### `GET /getVehicle`

#### Funcionalidade

Retorna o objeto do veiculo inteiro a partir do UUID que vai ser passado por parametro

#### Parametros

http://url:port/UUID

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
	"message": "missing token"
}
```

ou

```json
{
    "message": "Unauthorized"
}
```

**404 Not Found**
```
vehicle not found
```

**500 Internal Server Error**
```json
{
    "message": "Internal Server Error"
}
```