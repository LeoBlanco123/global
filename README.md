# Ecosfera Digital 🌎
![logo.png](documentacao%2Ffotos%2Flogo.png)

# Sumário
[Como rodar a aplicação](#como-rodar-a-aplicação)  
[Diagrama](#diagrama)  
[Link do Video](#link-do-video)

# Como rodar a aplicação

## Instruções para Rodar a Aplicação
### 1. Executar a Aplicação
- Navegue até o diretório: SRC > MAIN > JAVA > BR.COM.FIAP.ECOSFERA
- Execute a aplicação através da classe `GlobalSolutionApplication`.
### 2. Acessar a Aplicação Web
- Abra o seu navegador e digite a seguinte URL:
- http://localhost:8080/empresas
### 3. Configurações Necessárias
- Certifique-se de configurar o arquivo `application.properties` com as informações do seu banco de dados e o token do Spring AI.
### 4. Rodar a Mensageria
- Para iniciar o RabbitMQ, abra o Prompt de Comando (CMD) como administrador.
- Execute o seguinte comando (certifique-se de que o Docker Desktop está aberto e instalado):   
```docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management```


# Diagrama

## Diagrama UML
![model.png](documentacao%2Fdiagrama-classes%2Fmodel.png)

# Link do Video
https://www.youtube.com/watch?v=xZnYJc3lSt4
