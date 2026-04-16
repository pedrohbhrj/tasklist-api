# Tasklist API 📝

Uma API RESTful desenvolvida em **Java 21** com **Spring Boot** para o gerenciamento de usuários e suas respectivas tarefas. 

Este projeto foi construído com o objetivo de demonstrar a aplicação de boas práticas de desenvolvimento de software, arquitetura limpa, alta testabilidade e domínio de ferramentas fundamentais do ecossistema Spring e bibliotecas externas.

## 🚀 Tecnologias e Ferramentas

* **Java 21:** Uso de recursos modernos da linguagem, como `Records` para a camada de DTOs.
* **Spring Boot 3.x:** Framework base para a construção da API.
* **Spring Web:** Criação de endpoints RESTful com padrões de verbos HTTP e status codes adequados.
* **Spring Data JPA & Hibernate:** Persistência de dados, mapeamento objeto-relacional (ORM) e gerenciamento de transações (`@Transactional`).
* **PostgreSQL:** Banco de dados relacional escolhido para produção/desenvolvimento.
* **JUnit 5 & Mockito:** Frameworks utilizados para a criação de testes unitários e simulação de dependências (Mocks).
* **MapStruct:** Biblioteca para mapeamento seguro e performático entre Entidades e DTOs.
* **Lombok:** Redução de boilerplate code (Getters, Setters, Builders, Construtores).
* **Spring Validation:** Validação de dados de entrada via anotações (Ex: `@NotBlank`, `@Email`, `@CPF`, `@Future`).
* **Maven:** Gerenciamento de dependências e build do projeto.

## 🧠 Arquitetura e Padrões Aplicados

O projeto foi estruturado buscando alta coesão e baixo acoplamento, separando claramente as responsabilidades:

* **Padrão DTO (Data Transfer Object):** Isolamento das Entidades de banco de dados do tráfego web, garantindo que o cliente receba e envie apenas os dados estritamente necessários (Requests e Responses).
* **Padrão Controller-Service-Repository:** * `Controllers`: Lidam apenas com a camada HTTP, roteamento e respostas.
  * `Services`: Concentram as regras de negócio e validações complexas.
  * `Repositories`: Abstraem a complexidade das consultas ao banco de dados.
* **Testes Unitários Automatizados:** Validação de comportamento das regras de negócio (especialmente no `TarefaService`), garantindo que a lógica da aplicação funcione de forma independente e isolada do banco de dados.
* **Global Exception Handling:** Utilização de `@RestControllerAdvice` para capturar exceções (`NotFoundException`, `AlreadyExistsException`, falhas de validação) de forma global e retornar um JSON padronizado e amigável (via `ApiResponse`) para os clientes da API.
* **Mapeamento Relacional (1:N e N:1):** Relacionamento bidirecional mapeado corretamente entre `Usuario` e `Tarefa`, utilizando estratégias de deleção em cascata (`orphanRemoval`).
* **Paginação e Ordenação:** Implementação de retornos paginados (`Pageable`) nos endpoints de listagem para garantir performance e escalabilidade, com suporte à serialização moderna de páginas do Spring 3.3+ (`PagedModel`).
* **Consultas Customizadas:** Uso de `@NativeQuery` para lidar com buscas específicas (como encontrar tarefas atrasadas diretamente pelo banco).

## 🗂 Estrutura de Pacotes

```text
br.com.tasklist
├── controller/        # Endpoints da API (REST)
├── dto/               # Records para entrada e saída de dados
│   ├── request/
│   └── response/
├── entity/            # Entidades JPA (Mapeamento do Banco)
├── exception/         # Tratamento global de erros e exceções customizadas
├── mapper/            # Interfaces do MapStruct para conversão Entidade <-> DTO
├── repository/        # Interfaces Spring Data JPA
├── service/           # Regras de negócio
└── DataInitializer    # Classe (CommandLineRunner) para popular o banco em ambiente de dev

⚙️ Como Executar o Projeto
Pré-requisitos
Java 21+

Maven

PostgreSQL rodando localmente (porta 5432)

Passos para Execução
Clone o repositório:Aqui está o seu README.md completamente atualizado, formatado com as melhores práticas de Markdown, com a URL do repositório corrigida e com a nova seção de Testes Unitários destacando o seu uso de JUnit e Mockito.

Basta copiar o bloco de código abaixo e colar no seu arquivo:

Markdown
# Tasklist API 📝

Uma API RESTful desenvolvida em **Java 21** com **Spring Boot** para o gerenciamento de usuários e suas respectivas tarefas. 

Este projeto foi construído com o objetivo de demonstrar a aplicação de boas práticas de desenvolvimento de software, arquitetura limpa, alta testabilidade e domínio de ferramentas fundamentais do ecossistema Spring e bibliotecas externas.

## 🚀 Tecnologias e Ferramentas

* **Java 21:** Uso de recursos modernos da linguagem, como `Records` para a camada de DTOs.
* **Spring Boot 3.x:** Framework base para a construção da API.
* **Spring Web:** Criação de endpoints RESTful com padrões de verbos HTTP e status codes adequados.
* **Spring Data JPA & Hibernate:** Persistência de dados, mapeamento objeto-relacional (ORM) e gerenciamento de transações (`@Transactional`).
* **PostgreSQL:** Banco de dados relacional escolhido para produção/desenvolvimento.
* **JUnit 5 & Mockito:** Frameworks utilizados para a criação de testes unitários e simulação de dependências (Mocks).
* **MapStruct:** Biblioteca para mapeamento seguro e performático entre Entidades e DTOs.
* **Lombok:** Redução de boilerplate code (Getters, Setters, Builders, Construtores).
* **Spring Validation:** Validação de dados de entrada via anotações (Ex: `@NotBlank`, `@Email`, `@CPF`, `@Future`).
* **Maven:** Gerenciamento de dependências e build do projeto.

## 🧠 Arquitetura e Padrões Aplicados

O projeto foi estruturado buscando alta coesão e baixo acoplamento, separando claramente as responsabilidades:

* **Padrão DTO (Data Transfer Object):** Isolamento das Entidades de banco de dados do tráfego web, garantindo que o cliente receba e envie apenas os dados estritamente necessários (Requests e Responses).
* **Padrão Controller-Service-Repository:** * `Controllers`: Lidam apenas com a camada HTTP, roteamento e respostas.
  * `Services`: Concentram as regras de negócio e validações complexas.
  * `Repositories`: Abstraem a complexidade das consultas ao banco de dados.
* **Testes Unitários Automatizados:** Validação de comportamento das regras de negócio (especialmente no `TarefaService`), garantindo que a lógica da aplicação funcione de forma independente e isolada do banco de dados.
* **Global Exception Handling:** Utilização de `@RestControllerAdvice` para capturar exceções (`NotFoundException`, `AlreadyExistsException`, falhas de validação) de forma global e retornar um JSON padronizado e amigável (via `ApiResponse`) para os clientes da API.
* **Mapeamento Relacional (1:N e N:1):** Relacionamento bidirecional mapeado corretamente entre `Usuario` e `Tarefa`, utilizando estratégias de deleção em cascata (`orphanRemoval`).
* **Paginação e Ordenação:** Implementação de retornos paginados (`Pageable`) nos endpoints de listagem para garantir performance e escalabilidade, com suporte à serialização moderna de páginas do Spring 3.3+ (`PagedModel`).
* **Consultas Customizadas:** Uso de `@NativeQuery` para lidar com buscas específicas (como encontrar tarefas atrasadas diretamente pelo banco).

## 🗂 Estrutura de Pacotes

```text
br.com.tasklist
├── controller/        # Endpoints da API (REST)
├── dto/               # Records para entrada e saída de dados
│   ├── request/
│   └── response/
├── entity/            # Entidades JPA (Mapeamento do Banco)
├── exception/         # Tratamento global de erros e exceções customizadas
├── mapper/            # Interfaces do MapStruct para conversão Entidade <-> DTO
├── repository/        # Interfaces Spring Data JPA
├── service/           # Regras de negócio
└── DataInitializer    # Classe (CommandLineRunner) para popular o banco em ambiente de dev
⚙️ Como Executar o Projeto
Pré-requisitos
Java 21+

Maven

PostgreSQL rodando localmente (porta 5432)

Passos para Execução
Clone o repositório: git clone (https://github.com/pedrohbhrj/tasklist-api.git)
Configure o Banco de Dados:
Crie um banco de dados no PostgreSQL com o nome de tasksv3 (ou um nome de sua preferência).
Certifique-se de que as credenciais no arquivo src/main/resources/application.properties correspondem à sua instalação local:

Properties
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
Nota: Nunca versione senhas reais em repositórios públicos.

Compile o projeto e gere os Mappers:
Na raiz do projeto, execute o comando Maven para compilar as classes (e gerar as implementações do MapStruct):

mvn clean install
(Opcional) Execute os Testes Unitários:
Para rodar a suíte de testes construída com JUnit e Mockito:

Bash
mvn test
Inicie a aplicação:
Inicie a aplicação executando a classe TasklistApplication pela sua IDE ou via terminal:

Bash
mvn spring-boot:run
O DataInitializer já irá inserir dados de teste no banco automaticamente na primeira execução.

🔗 Principais Endpoints
A API expõe diversas rotas, todas encapsuladas no padrão ApiResponse<T>. Algumas das principais são:

Usuários (/api/usuario)
POST /api/usuario - Cria um novo usuário.

GET /api/usuario/todos - Retorna lista paginada de usuários.

PUT /api/usuario/{id} - Atualiza dados do usuário (merge seletivo).

Tarefas (/api/tarefa)
POST /api/tarefa/{usuarioId} - Cria uma tarefa vinculada a um usuário.

GET /api/tarefa/atrasadas - Retorna lista paginada de tarefas cujo prazo expirou.

PATCH /api/tarefa/{id}?estaConcluida=true - Atualiza apenas o status de conclusão da tarefa.

GET /api/tarefa/{estaConcluida} - Filtra tarefas por status de conclusão.
```
