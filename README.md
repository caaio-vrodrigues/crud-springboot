# CRUD-SPRINGBOOT - BACKEND PARA AUTENTICAÇÃO DE LOGIN

API REST construída em Spring Boot 3.5, responsável pela autenticação (login), autorização e exposição de recursos protegidos consumidos pelo frontend Next.js.

- Produção (Frontend): https://projeto-login-client-side.vercel.app/ 
- Repositório deste Backend (Spring Boot): https://github.com/caaio-vrodrigues/crud-springboot 

---

## Sumário

- [Arquitetura e Fluxo](#arquitetura-e-fluxo)
- [Tecnologias](#tecnologias)
- [CORS](#cors)
- [Contrato de Erros](#contrato-de-erros)
- [Referência da API](#referência-da-api)
- [Segurança](#segurança)
- [Troubleshooting](#troubleshooting)

---

## Arquitetura e Fluxo

- O frontend envia credenciais para o endpoint de login do backend.
- O backend valida as credenciais, emite um JWT (e opcionalmente um refresh token).
- O frontend armazena o token.
- Para acessar rotas protegidas, o frontend envia o header Authorization: Bearer.
- Erros seguem um formato padronizado (ErrorPayload) com timestamp, status, message e path.

---

## Tecnologias

- Java 21
- Spring Boot 3.5.4
- spring-boot-starter-web
- spring-boot-starter-security
- spring-boot-starter-validation
- Banco de dados H2 (não persistente)
- Maven 3.9.11
- JWT (autenticação Bearer)
- Adapta One, com mais de 20 modelos de IA diferentes (Chat GPT-5 para auxílio nesse projeto).

---

## CORS

- Conexão via Cross Origin.
- Url para conexão com o frontend: "https://projeto-login-client-side.vercel.app".

---

## Contrato de Erros

1. O backend utiliza um payload de erro padronizado.

2. Campos utilizados:

- timestamp: string ISO-8601 com o momento do erro.
- status: código HTTP.
- message: descrição legível do erro.
- path: caminho da requisição.

---

## Referência da API

1. Todas as rotas protegidas exigem o header Authorization: Bearer.

2. Principais rotas:

- url-API/auth/ping - Faz um ping para realizar warm no servidor ao abrir a aplicação.
- url-API/auth/create - Cria novo usuário e salva credenciais no db.
- url-API/auth/login - Autentica o usuário e retorna token.

3. Códigos de Status

- 200 OK: sucesso.
- 201 Created: recurso criado (Location header).
- 204 No Content: operação sem body.
- 400 Bad Request: validação.
- 401 Unauthorized: não autenticado ou token inválido.
- 403 Forbidden: sem permissão.
- 404 Not Found: não encontrado.
- 409 Conflict: conflito de recurso (ex.: e-mail já existente).
- 422 Unprocessable Entity: regras de negócio/validação específica.
- 429 Too Many Requests: rate limit (se habilitado).
- 500 Internal Server Error: erro inesperado.

---

## Segurança

- Utilização de Token JWT.
- CORS habilitado para o domínio do frontend em produção.
- Retorno de mensagem de erro genérica para não vazar detalhes.
- Validação com Bean (@Valid, @NotBlank, @Email, etc.).

---

## Troubleshooting

1. 401 em rotas protegidas

- Verifique header Authorization: Bearer.
- Confirme expiração e assinatura do JWT (segredo correto).

2. CORS bloqueando

- Garanta que o domínio do frontend está em CORS_ALLOWED_ORIGINS.

3. 500 genérico

- Confira logs do servidor e a causa-raiz.

4. Erro de conexão com DB

- Ajuste SPRING_DATASOURCE_URL/USERNAME/PASSWORD e o driver
