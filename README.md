### Tech Stack
- Java 11
- Spring Boot
- Spring Security
- Docker
- MySQL

### O que é ?
Um exemplo de aplicação que trabalha com autênticação, utilizando spring-security e JWT. \

Para isso desenvolvi 2 aplicações em Java e 1 jwt-lib: 
- autenticacao-app: Uma aplicação responsável por realizar uma autênticação, através de Login/Senha.
- business-app: Uma aplicação responsável por disponibilizar API's de uma biblioteca fake, em que alguns endpoints serão acessadas somente se realizarem autênticação anteriormente.
- jwt-lib: Um jar responsável por fazer encode/decode JWT.

### Arquitetura
![](https://github.com/lucianoortizsilva/login-spring-security/blob/a57245e91d7c04f0e7f0fe0e7726f604f7c050e0/arquitetura.png?raw=true)

### Como rodar ?
> 1º Inicializar banco de dados, via docker-compose:
- Na raiz do repositório, execute **`docker-compose up`**

<br/>

> 2º Build `jwt-lib`:
- No diretório jwt-lib: **`mvn clean install`**

<br/>

> 3º Build e Deploy `autenticacao-app`:
- No diretório autenticacao-app: **`mvn clean package`**
- No diretório autenticacao-app: **`mvn spring-boot:run`**

<br/>

> 4º Build e Deploy `business-app`:
- No diretório business-app: **`mvn clean package`**
- No diretório business-app: **`mvn spring-boot:run`**
