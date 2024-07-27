
# Desafio de Projeto Design Patterns com Java e Spring Framework

Esta é uma API RESTful para gerenciamento de clientes, construída com Spring Boot. A aplicação utiliza vários padrões de projeto, incluindo Singleton, Strategy e Facade, para manter uma arquitetura limpa e eficiente.

## Funcionalidades

- **CRUD de Clientes**: Permite criar, ler, atualizar e deletar informações de clientes.
- **Integração com ViaCEP**: Realiza consultas de endereço via API do ViaCEP.

## Padrões de Projeto Utilizados

- **Singleton**: A anotação `@Service` garante que `ClienteServiceImpl` seja um singleton gerenciado pelo Spring.
- **Strategy**: A interface `ClienteService` permite múltiplas implementações, embora atualmente haja apenas uma.
- **Facade**: A classe `ClienteRestController` atua como uma fachada, abstraindo a complexidade das integrações com o banco de dados e a API do ViaCEP.

## Alterações Realizadas

1. **Validações de Entrada e Tratamento de Erros**

   - Adicionadas validações de entrada no método `inserir` para garantir que o CEP do cliente seja válido.
   - Tratamento de erros no método `salvarClienteComCep` para lançar exceções adequadas quando o CEP não é encontrado.

2. **Refatoração do `ClienteRestController`**

   - Melhorado o manuseio das respostas HTTP, incluindo o retorno de status apropriados como `201 Created` para inserções bem-sucedidas e `404 Not Found` quando um cliente não é encontrado.

3. **Tratamento de Exceções no `ViaCepService`**

   - Adicionado tratamento de exceções para lidar com erros ao consultar o serviço ViaCEP.


## Dependências

- Spring Boot
- Spring Data JPA
- Spring Web
- Spring Cloud OpenFeign
- H2 Database
- Swagger UI

## Configuração

### 1. Clone o Repositório

```sh
git clone https://github.com/danaraujoc/cliente-api.git
cd cliente-api

```

### 2. Configure o Banco de Dados
A aplicação usa o banco de dados H2 para facilitar o desenvolvimento. As configurações padrão podem ser encontradas no arquivo `src/main/resources/application.properties`.

### 3. Execute a Aplicação
Você pode executar a aplicação usando o Maven ou uma IDE como IntelliJ IDEA ou Eclipse.


Copiar código
```sh 
mvn spring-boot:run

```

A aplicação estará disponível em http://localhost:8080/swagger-ui/index.html.

## Endpoints

Listar Todos os Clientes
```sh 
GET /clientes
```
Buscar Cliente por ID
```sh 
GET /clientes/{id}
```
Inserir Novo Cliente
```sh 
POST /clientes
Content-Type: application/json

{
  "nome": "Nome do Cliente",
  "endereco": {
    "cep": "12345678"
  }
}
```
Atualizar Cliente
```sh 
PUT /clientes/{id}
Content-Type: application/json

{
  "nome": "Nome Atualizado",
  "endereco": {
    "cep": "12345678"
  }
}
```
Deletar Cliente
```sh 
DELETE /clientes/{id}
```

## Referência

 - [Repositório de Referência do Projeto](https://github.com/digitalinnovationone/lab-padroes-projeto-spring)
 - [Slides](https://docs.google.com/presentation/d/1WU8gLHbB1s9XCIGsQ87gD36kt398qLch/edit#slide=id.geb2cb828ca_0_17)

