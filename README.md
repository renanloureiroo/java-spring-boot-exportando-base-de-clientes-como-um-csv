# Exportação de Clientes para CSV - Spring Boot

Este projeto exporta uma base de clientes do banco PostgreSQL para um arquivo CSV de forma eficiente, utilizando Java, Spring Boot, JPA e Docker.

## Pré-requisitos

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado em sua máquina (inclui Docker Compose Desktop)
- Git instalado (opcional, para clonar o repositório)

## Como rodar o projeto

1. **Clone o repositório (ou baixe os arquivos):**

   ```sh
   git clone <url-do-repositorio>
   cd <nome-do-repository>
   ```

2. **Abra o Docker Desktop e certifique-se de que ele está rodando.**

3. **No terminal, execute o comando abaixo para subir a aplicação e o banco:**

   ```sh
   docker-compose up --build
   ```

   Isso irá:

   - Construir a imagem da aplicação Java
   - Subir o banco PostgreSQL já configurado
   - Rodar a aplicação Spring Boot

4. **Aguarde a inicialização:**

   - O seed irá criar automaticamente 1 milhão de clientes no banco na primeira execução (pode demorar alguns minutos).

5. **Acesse a aplicação:**

   - Documentação Swagger: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
   - Endpoints principais:
     - Exportação com arquivo temporário: `GET /clients/export`
     - Exportação por streaming direto: `GET /clients/export-stream`

6. **Baixe o arquivo CSV:**
   - Use o Swagger ou acesse os endpoints diretamente no navegador ou via ferramentas como Postman.

## Observações

- O projeto foi pensado para consumir pouca memória, mesmo exportando milhões de registros.
- O limite de memória do container pode ser ajustado no `docker-compose.yml` para testar a eficiência.
- O arquivo CSV gerado é compatível com Excel, Numbers e Google Sheets.

## Limpeza

- Para parar e remover os containers:
  ```sh
  docker-compose down
  ```
- Para remover volumes e dados do banco:
  ```sh
  docker-compose down -v
  ```

---

Se tiver dúvidas ou sugestões, fique à vontade para abrir uma issue ou contribuir!
