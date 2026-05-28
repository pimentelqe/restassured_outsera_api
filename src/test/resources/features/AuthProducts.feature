#language:pt

Funcionalidade: Realizar Login
  Testes da API de Login

  Cenario: Realizar login com sucesso
    Dado que tenha um payload valido da API de login
    Quando envio uma requisicao do tipo POST de login
    Entao Valido o status 200 no response
    E Valido que o response contem o campo "accessToken"
    E armazeno o token que recebo do response


  Esquema do Cenario: Realizar login com credenciais invalidas - <cenario>
    Dado que tenha um payload da API de login com as seguintes informacoes
      | username | <username> |
      | password | <password> |
    Quando envio uma requisicao do tipo POST de login
    Entao Valido o status 400 no response
    E Valido que a mensagem de erro e "Invalid credentials"

    Exemplos:
      | cenario          | username           | password |
      | usuario invalido | invalido@email.com | 123456   |
      | senha invalida   | test@email.com     | invalido |


  Cenario: Realizar login sem informar username
    Dado que tenha um payload da API de login com as seguintes informacoes
      | password | qualquersenha |
    Quando envio uma requisicao do tipo POST de login
    Entao Valido o status 400 no response
    E Valido que a mensagem de erro e "Username and password required"


  Cenario: Realizar login sem informar password
    Dado que tenha um payload da API de login com as seguintes informacoes
      | username | algumuser |
    Quando envio uma requisicao do tipo POST de login
    Entao Valido o status 400 no response
    E Valido que a mensagem de erro e "Username and password required"
