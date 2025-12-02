# **📠 Controle de Estoque**

### Índice

- [📌 Introdução](#introducao)
- [🔌 Endpoints](#endpoints)
- [🧰 Requisitos](#requisitos)
- [⚙️ Como Instalar e Rodar](#como-instalar-e-rodar)
- [🔹 Tecnologias Utilizadas](#tecnologias-utlizadas)
- [📒 Licença](#licenca)

## 📌 Introdução
<a name="introducao"></a>
Este projeto é uma API RESTful para o Controle de Estoque de produtos, construída utilizando o framework Spring Boot. <br> <br>
O objetivo é fornecer uma backend robusto para gerenciar produtos, suas categorias e o nível de estoque correspondente.<br> <br>
A aplicação segue a arquitetura de microservices e o padrão MVC (Model-View-Controller) é aplicado implicitamente na organização do Spring Boot.

--- 

## 🔌 Endpoints
<a name="endpoints"></a>

🔹**Produtos**
| Método | Endpoint | Descrição |
| --- | --- | --- |
| GET	| /api/produtos | Lista todos os produtos |
| GET	| /api/produtos/{id} | Obtém um produto por ID |
| POST | /api/produtos | Cria um novo produto |
| PUT | /api/produtos/{id} | Atualiza um produto |
| DELETE | /api/produtos/{id} | Deleta um produto |

🔹**Fornecedores**
| Método | Endpoint | Descrição |
| --- | --- | --- |
| GET	| /api/fornecedores | Lista todos os fornecedores |
| GET	| /api/fornecedores/{id} | Obtém um fornecedor por ID |
| POST | /api/fornecedores | Cria um novo fornecedor |
| PUT	| /api/fornecedores/{id} | Atualiza um fornecedor |
| DELETE | /api/fornecedores/{id} | Deleta um fornecedor |

🔹**Vendas**
| Método | Endpoint | Descrição |
| --- | --- | --- |
| GET | /api/vendas | Lista todas as vendas |
| GET | /api/vendas/{id} | Obtém uma venda por ID |
| POST | /api/vendas | Cria uma nova venda |
| PUT | /api/vendas/{id} | Atualiza uma venda |
| DELETE | /api/vendas/{id} | Deleta uma venda |

🔹**Clientes**
| Método | Endpoint | Descrição |
| --- | --- | --- |
| GET | /api/clientes | Lista todos os clientes |
| GET | /api/clientes/{id} | Obtém um cliente por ID |
| POST | /api/clientes | Cria um novo cliente |
| PUT | /api/clientes/{id} | Atualiza um cliente |
| DELETE | /api/clientes/{id} | Deleta um cliente |

---

## 🧰 Requisitos
<a name="requisitos"></a>

🔹 **JDK**: 17+ (verifique em pom.xml)<br>
🔹 **Maven**: use o wrapper incluído (mvnw / mvnw.cmd)<br>
🔹 **Banco de dados**: configure credenciais em src/main/resources/application.properties

---

## ⚙️ Como Instalar e Rodar
<a name="como-instalar-e-rodar"></a>

### **Clone o repositório:**

```bash
git clone https://github.com/ThiagoLMattos/ControleEstoque-20240125.git
cd ControleEstoque-20240125

```

### **Compile e Empacote o Projeto (Maven): **

```bash
# Baixa dependências e gera o arquivo .jar na pasta /target
mvn clean package
```

### **Execute a Aplicação: **

```bash
# Inicia a aplicação Spring Boot
java -jar target/controleEstoque-0.0.1-SNAPSHOT.jar
```

**A API estará acessível em http://localhost:8080.**

---

## 💠 Tecnologias utilizadas
<a name="tecnologias-utilizadas"></a>
O projeto foi construído sobre o ecossistema <br>
🔹 **Java/Spring:Spring Boot**: Para simplificar a configuração e execução da API.<br>
🔹 **Spring Data JPA / Hibernate**: Para persistência de dados e mapeamento objeto-relacional.<br>
🔹 **Jackson**: Para serialização e desserialização JSON (incluindo tratamento de Lazy Loading do Hibernate).<br>
🔹 **Maven**: Gerenciamento de dependências.<br> <br>
[![My Skills](https://skillicons.dev/icons?i=java&theme=light)](https://skillicons.dev)

---

## 📒 Licença
<a name="licenca"></a>
Esse projeto foi construído por fins educativos.
