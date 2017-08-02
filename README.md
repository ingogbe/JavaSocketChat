# README #

Trabalho de Redes usando ServerSocket em Java

### Projetos inclusos no repositório

```sh
1 TrabRedes-Client
2 TrabRedes-Framework
3 TrabRedes-Server
```

### Alterações antes do uso

É necessário fazer alteração na constante `SERVER_STORAGE` no arquivo `MainServer.java` no projeto `TrabRedes-Server`, para o local onde deseja que o servidor utilize par armazenar os arquivos.

```sh
\trabalhoredes\TrabRedes-Server\src\MainServer.java
```

```java
public static final String SERVER_STORAGE = "C:/Users/Mineradora03/Desktop/serverStorage/";
```

### Configuração antes do uso (Eclipse IDE)

- Crie todos os projetos
    * Vá em **File**
    * Em seguida em **New**
    * E clique na opção **Java Project**
    * Deselecione a opção **Use default location**
    * Clique em **Browse**
    * Navega para a pasta onde deu clone ou salvou o repositorio
    * Selecione o projeto que deseja criar (Projetos 1, 2 e 3 citados anteriormente)
    * Clique em **Finish**
    * Faça isso para os três projetos
    

- Faça a ligação do projeto `TrabRedes-Framework` com os projetos `TrabRedes-Client` e `TrabRedes-Framework`
    * Clique com o botão direito sobre o projeto **TrabRedes-Client**
    * Vá até a opção **Build path**
    * Clique na opção **Configure Build Path...**
    * Na janela que se abriu. Clique na aba **Projects**
    * Clique no botão **Add**
    * Selecione o projeto **TrabRedes-Framework**
    * Clique em **Ok** e em seguida em **Apply and Close**

### Uso

* Inicie primeiro o Server (MainServer.java)
* Inicie quantos Clientes desejar (MainClient.java)
* Para conectar ao servidor usando o Cliente, insera o IP mostrado na tela do Servidor no campo IP do Cliente.
