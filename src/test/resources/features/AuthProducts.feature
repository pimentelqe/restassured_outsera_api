#language:pt

Funcionalidade: Realizar Login
  Testes da API de Login

  Cenario: Relizar login com sucesso
    Dado que tenha um payload valido da API de login
    Quando envio uma requisicao do tipo POST de login
    Entao Valido o status 200 no response
    E armazeno o token que recebo do response


  Esquema do Cenario:  Realizar login com <cenario>
    Dado que tenha um payload da API de login com as seguintes informacoes
      | username | <username> |
      | senha    | <senha>    |
    Quando envio uma requisicao do tipo POST de login
    Entao Valido o status 400 no response

    Exemplos:
      | cenario         | username           | senha    |
      | usario invalido | invalido@email.com | 123456   |
      | senha invalida  | test@email.com     | invalido |
