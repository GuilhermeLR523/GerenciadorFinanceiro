# 💰 Gerenciador Financeiro

Aplicação desktop desenvolvida em **Java** para auxiliar no controle e acompanhamento de finanças pessoais.

O sistema permite organizar contas, registrar transações, acompanhar informações financeiras por meio de um dashboard e definir metas. O projeto foi desenvolvido com foco na aplicação prática de conceitos de desenvolvimento desktop, persistência de dados, organização de código e testes.

## 📌 Versão

**Versão atual: 1.0**

A versão 1.0 contém as principais funcionalidades planejadas para a primeira versão do sistema. O projeto continuará recebendo melhorias e novas funcionalidades futuramente.

## 🚀 Funcionalidades

* Gerenciamento de contas
* Registro e acompanhamento de transações
* Controle de receitas e despesas
* Dashboard com informações financeiras
* Criação e acompanhamento de metas financeiras
* Persistência local dos dados
* Interface gráfica desktop

## 🛠️ Tecnologias utilizadas

### Desenvolvimento

* Java
* JavaFX
* FXML
* CSS

### Banco de Dados

* SQLite
* JPA
* Hibernate

### Gerenciamento e Testes

* Maven
* JUnit

### Distribuição

* jpackage
* WiX Toolset

## 🗄️ Persistência de dados

O sistema utiliza **SQLite** como banco de dados local.

A comunicação entre a aplicação e o banco é realizada utilizando **JPA e Hibernate**, responsáveis pelo mapeamento e persistência das entidades da aplicação.

## 🖥️ Interface

A interface gráfica foi desenvolvida utilizando **JavaFX**, com as telas estruturadas através de arquivos **FXML** e estilizadas utilizando **CSS**.

O sistema possui diferentes áreas para acompanhamento e gerenciamento das informações financeiras, incluindo:

* Dashboard
* Contas
* Transações
* Metas

## 🧪 Testes

O projeto utiliza **JUnit** para a implementação de testes automatizados, auxiliando na validação do comportamento das funcionalidades da aplicação.

## 📦 Instalação

O projeto utiliza **jpackage** para gerar uma versão instalável da aplicação desktop.

No Windows, o **WiX Toolset** é utilizado durante o processo de geração do instalador.

O instalador da versão estável poderá ser encontrado na seção **Releases** deste repositório.

## ▶️ Executando pelo código-fonte

### Pré-requisitos

Para executar o projeto pelo código-fonte, é necessário possuir:

* Java JDK compatível com o projeto
* Maven
* Git
* IDE com suporte a projetos Java, como IntelliJ IDEA

Clone o repositório:

```bash
git clone https://github.com/GuilhermeLR523/GerenciadorFinanceiro.git
```

Entre na pasta do projeto:

```bash
cd GerenciadorFinanceiro
```

Instale as dependências e compile o projeto:

```bash
mvn clean install
```

O projeto também pode ser importado diretamente em uma IDE utilizando o arquivo `pom.xml`.

## 📂 Estrutura do projeto

```text
GerenciadorFinanceiro/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
├── pom.xml
├── .gitignore
└── README.md
```

## 📈 Próximas versões

O projeto continuará sendo desenvolvido após a versão 1.0, com melhorias de interface, funcionalidades e organização interna conforme novas necessidades forem identificadas.

## 👨‍💻 Autor

**Guilherme Leite Rocha**

* GitHub: GuilhermeLR523
* Estudante de Análise e Desenvolvimento de Sistemas — COTEMIG

## 📄 Licença

Projeto desenvolvido para fins de estudo e portfólio.
