# Desafio Backend Itaú

Este é um projeto feito com base no desafio proposto pelo Itaú alguns anos atrás. As requisições técnicas podem ser visualizadas através [desse repositório.](https://github.com/rafaellins-itau/desafio-itau-vaga-99-junior) Em suma, o desafio propõe a criação de uma API Restful que recebe Transações, retorna estatísticas e que carregue dados em memória.

## 1. Tech Stack

- Java 21
- Spring Boot 3
- Docker

## 2. Pré-requisitos

- Java JDK 21
- Maven
- Docker

## 3. Arquitetura

Escolhi um modelo de arquitetura em camadas para desenvolver o projeto:

- `controllers` → Requisições HTTP
- `dtos` → Transferência de dados entre camadas
- `exceptions` → Exceções customizadas
- `infra` → Infraestrutura
- `mappers` → Conversão DTO - Entidade
- `models` → Entidade
- `services` → Regras de negócio
- `storage` → Armazenamento em memória

## 4. Endpoints

| Método | Caminho        | Descrição                                                      |
| ------ | -------------- | -------------------------------------------------------------- |
| POST   | `/transacao`   | Cria uma nova transação.                                       |
| DELETE | `/transacao`   | Limpa o armazenamento em memória.                              |
| GET    | `/estatistica` | Recupera estatísticas das transações criadas no último minuto. |

## 5. Como Executar

### 1. Clone o repositório:
```bash
git clone https://github.com/tuchanski/desafio-backend-itau.git
cd desafio-backend-itau
```

### 2. Gere o JAR da aplicação
```bash
mvn clean package
```

### 3. Builde a imagem Docker
```bash
docker build -t transacoes .
```

### 4. Rode o container
```bash
docker run -p 8080:8080 transacoes
```

### 5. Agora, acesse a documentação da API no navegador:
```bash
http://localhost:8080/swagger-ui/index.html
```

## 6. Autor

Feito por [Guilherme Tuchanski](https://github.com/tuchanski) com fins educacionais.