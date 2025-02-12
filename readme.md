Descrição:
Esta MR entrega a implementação completa da API de Cadastro de Chaves PIX, atendendo a todos os requisitos mínimos do desafio proposto pelo Itaú. A solução foi desenvolvida utilizando Java 17, Spring Boot 3.4.2 e banco de dados H2 para persistência.

Principais Implementações:

Inclusão de Chaves PIX:

Suporte para CPF, e-mail e celular.
Validações rigorosas para cada tipo de chave conforme especificações do desafio.
Limitação de 5 chaves para pessoa física e 20 chaves para pessoa jurídica.
Prevenção de duplicidade de chaves para diferentes correntistas.
Geração automática de UUID como identificador único da chave.
Registro de data e hora no momento da criação.
Alteração de Chaves PIX:

Permite atualização de informações da conta vinculada.
Restrições para impedir alteração de chaves inativas, tipo ou valor da chave.
Validações específicas para tipo de conta, número da agência e conta, nome e sobrenome do correntista.
Inativação de Chaves PIX:

Chave inativada não pode ser alterada ou reutilizada.
Registro de data e hora da inativação.
Impede inativação de chaves já inativas com retorno de erro apropriado.
Consultas de Chaves PIX:

Consulta por ID (obrigatória).
Consulta por tipo de chave e por agência e conta.
Regras para impedir combinações inválidas de filtros e retorno adequado em caso de não encontrar registros.

## Inclusão

### Objetivo

Viabilizar a inclusão de chaves PIX, vinculando a chave à agência e conta do correntista Itaú.

Para a implementação dessa funcionalidade, minimamente deve estar contemplado o desenvolvimento de três dos cinco tipos de chaves. Fique livre a escolha por parte do candidato. Exemplo: Inclusão pode contemplar somente para os tipos de chaves celular, e-mail e CPF.

### Dados de entrada da inclusão (Tabela 1)

| Nome                  | Tipo Dado                                         | Obrigatório |
|-----------------------|---------------------------------------------------|-------------|
| TIPO CHAVE            | (celular \| email \| cpf \| cnpj \| aleatorio)    | SIM         |
| VALOR CHAVE           | Texto (77)                                        | SIM         |
| TIPO CONTA            | (corrente \| poupança) Texto (10)                 | SIM         |
| NUMERO AGENCIA        | Numérico (4)                                      | SIM         |
| NUMERO CONTA          | Numérico (8)                                      | SIM         |
| NOME CORRENTISTA      | Texto (30)                                        | SIM         |
| SOBRENOME CORRENTISTA | Texto (45)                                        | NÃO         |

### Critérios de aceite

1. Deve registrar em banco de dados as informações imputadas.
2. Deve gerar um código de registro único (id), independentemente do tipo de chave registrado (celular, e-mail, CPF, CNPJ etc...).
   - A chave (ID) deve ser no formato UUID.
3. Limitar em até 5 chaves por conta para pessoa física e 20 chaves para pessoa jurídica.
4. Não deve permitir o registro de chaves duplicadas. O valor informado no campo VALOR CHAVE, não deve existir para outro correntista do banco.
5. Deve ser registrado a data e hora em que a chave foi registrada.
6. Deve validar as regras de cadastro seguindo os tipos e regras abaixo:
   - **Celular**:
      - Deve validar se valor já existe cadastrado.
      - Deve possuir o código país:
         - Deve ser numérico (não aceitar letras).
         - Deve ser de até dois dígitos.
         - Deve iniciar com o símbolo “+”.
      - Deve possuir DDD:
         - Deve ser numérico (não aceitar letras).
         - Deve ser de até três dígitos.
      - Número com nove dígitos:
         - Deve ser numérico (não aceitar letras).
   - **E-mail**:
      - Deve validar se valor já existe cadastrado.
      - Deve conter o símbolo “@”.
      - Pode conter valores alfanuméricos.
      - Máximo de 77 caracteres.
   - **CPF**:
      - Deve validar se valor já existe cadastrado.
      - Deve conter no máximo 11 dígitos.
      - Deve fazer validação de CPF válido.
      - Deve aceitar somente números.
   - **CNPJ**:
      - Deve validar se valor já existe cadastrado.
      - Deve conter no máximo 14 dígitos.
      - Deve fazer validação de CNPJ válido.
      - Deve aceitar somente números.
   - **Chave Aleatória**:
      - Deve validar se valor já existe cadastrado.
      - Deve aceitar no máximo 36 caracteres alfanuméricos.
   - **TIPO CONTA**:
      - Somente permite os valores (corrente ou poupança).
      - Deve ser informado obrigatoriamente.
      - Não deve permitir estourar 10 caracteres.
   - **NÚMERO AGÊNCIA**:
      - Deve permitir somente valores numéricos.
      - Deve ser informado obrigatoriamente.
      - Não deve permitir estourar 4 dígitos.
   - **NÚMERO CONTA**:
      - Deve permitir somente valores numéricos.
      - Deve ser informado obrigatoriamente.
      - Não deve permitir estourar 8 dígitos.

7. Deve respeitar a obrigatoriedade dos campos (Tabela 1).
8. Deve exibir mensagem de erro (texto livre), caso regra não seja respeitada.
9. Retornar http code 200 caso inclusão seja realizada com sucesso. Retornar no corpo da resposta o id gerado.
10. Retornar http code 422 caso inclusão não respeite as regras de validação.

## Alteração

### Objetivo

Permitir alteração do valor de uma chave registrada. Deve-se permitir alterar um e-mail, telefone, CNPJ / CPF já cadastrado.

### Dados de entrada alteração (Tabela 2)

| Nome                  | Tipo Dado              | Obrigatório |
|-----------------------|------------------------|-------------|
| ID                    | UUID                   | SIM         |
| TIPO CONTA            | (corrente \| poupança) | SIM         |
| NUMERO AGENCIA        | Texto (10)             | SIM         |
| NUMERO CONTA          | Numérico (4)           | SIM         |
| NOME CORRENTISTA      | Texto (30)             | SIM         |
| SOBRENOME CORRENTISTA | Texto (45)             | NÃO         |

### Dados de saída (Tabela 3)

| Nome                  | Tipo Dado              |
|-----------------------|------------------------|
| ID                    | UUID                   |
| TIPO CHAVE            | (celular \| email \| cpf \| cnpj \| aleatorio) |
| VALOR CHAVE           | Texto (77)             |
| TIPO CONTA            | (corrente \| poupança) |
| NUMERO AGENCIA        | Texto (10)             |
| NUMERO CONTA          | Numérico (4)           |
| NOME CORRENTISTA      | Texto (30)             |
| SOBRENOME CORRENTISTA | Texto (45)             |
| DATA HORA INCLUSAO DA CHAVE | DATETIME        |

### Critérios de aceite

1. Deve ser feita a validação do valor alterado, conforme tipo de dado e se a informação é obrigatória ou não.
2. O ID da chave **NÃO** pode ser alterado.
3. O tipo da chave **NÃO** pode ser alterado.
4. O valor da chave **NÃO** pode ser alterado.
5. **Não** é permitido alterar chaves inativadas.
6. Deve validar os valores alterados seguindo regras abaixo:
   - **TIPO CONTA**:
      - Somente permite os valores (corrente ou poupança).
      - Deve ser informado obrigatoriamente.
      - Não deve permitir estourar 10 caracteres.
   - **NÚMERO AGÊNCIA**:
      - Deve permitir somente valores numéricos.
      - Deve ser informado obrigatoriamente.
      - Não deve permitir estourar 4 dígitos.
   - **NÚMERO CONTA**:
      - Deve permitir somente valores numéricos.
      - Deve ser informado obrigatoriamente.
      - Não deve permitir estourar 8 dígitos.
   - **NOME CORRENTISTA**:
      - Deve ser informado obrigatoriamente.
      - Não deve permitir estourar 30 caracteres.
   - **SOBRENOME CORRENTISTA**:
      - Se informado, não deve permitir estourar 45 caracteres.

7. Deve exibir mensagem de erro (texto livre), caso regra não seja respeitada.
8. Retornar http code 200 caso a alteração seja realizada com sucesso. Retornar no corpo da resposta o conteúdo da Tabela 3.
9. Retornar http code 422 caso a alteração não respeite as regras de validação.
10. Retornar http code 404 caso o ID informado não seja encontrado.

## Deleção

### Objetivo

Inativar uma chave registrada por ID, impedindo que a mesma seja alterada ou consultada. Somente o ID da chave deve ser informado para efetivar a desativação.

### Dados de saída (Tabela 4)

| Nome                  | Tipo Dado              |
|-----------------------|------------------------|
| ID                    | UUID                   |
| TIPO CHAVE            | (celular \| email \| cpf \| cnpj \| aleatorio) |
| VALOR CHAVE           | Texto (77)             |
| TIPO CONTA            | (corrente \| poupança) |
| NUMERO AGENCIA        | Texto (10)             |
| NUMERO CONTA          | Numérico (4)           |
| NOME CORRENTISTA      | Texto (30)             |
| SOBRENOME CORRENTISTA | Texto (45)             |
| DATA HORA INCLUSAO DA CHAVE | DATETIME        |
| DATA HORA INATIVAÇÃO DA CHAVE | DATETIME     |

### Critérios de aceite

1. Se o ID da chave informada já estiver desativado, uma mensagem deve ser retornada informando que a mesma já foi desativada (texto da mensagem livre).
   - Retornar http code 422.
2. Deve ser registrada a data e a hora da solicitação da desativação da chave.
3. Retornar em caso de sucesso:
   - http code 200.
   - Payload conforme Tabela 4.

## Consulta

### Objetivo

Disponibilizar funcionalidades de consulta de chaves PIX por:

a) Consulta por ID  
b) Consulta por Tipo de chave  
c) Agência e Conta  
d) Nome do correntista  
e) Data de inclusão da chave  
f) Data da inativação da chave

Para a implementação dessa funcionalidade, minimamente deve estar contemplado o desenvolvimento de três das seis consultas. **A consulta por ID é obrigatória!** Fique livre a escolha por parte do candidato, quais consultas implementar.

### Dados de saída (Tabela 5)

| Nome                      | Tipo Dado              |
|---------------------------|------------------------|
| ID                        | UUID                   |
| TIPO CHAVE                | (celular \| email \| cpf \| cnpj \| aleatorio) |
| VALOR CHAVE               | Texto (77)             |
| TIPO CONTA                | (corrente \| poupança) |
| NUMERO AGENCIA            | Texto (10)             |
| NUMERO CONTA              | Numérico (4)           |
| NOME CORRENTISTA          | Texto (30)             |
| SOBRENOME CORRENTISTA     | Texto (45)             |
| DATA HORA INCLUSAO DA CHAVE | DATE (dd/mm/aaaa)   |
| DATA HORA INATIVAÇÃO DA CHAVE | DATE (dd/mm/aaaa)|

### Critérios de aceite

1. Disponibilizar consultas combinadas entre os filtros **b**, **c**, **d**, **e** ou **f**.
2. Se informar o **ID** para consulta, nenhum outro filtro pode ser aceito.
   - Devolver http code 422 com mensagem (texto livre).
3. Não permitir a combinação de filtros **Data de inclusão da chave** e **Data da inativação da chave**. Somente um ou outro.
   - Devolver erro 422 com mensagem (texto livre).
4. Caso a consulta não retorne registros:
   - Devolver http code 404.
5. A lista resultado deve conter os parâmetros da Tabela 5, independente do filtro realizado.
6. Campos nulos devem ser apresentados como branco `""`.

---

## O seu objetivo é:

1. Escrever um código funcional de um cadastro de Chaves PIX com APIs que atendam as funcionalidades acima descritas.
2. Utilizar preferencialmente a linguagem **Java**.
3. Seu código deve possuir testes unitários (90% de cobertura).
4. Utilizar banco de dados (livre escolha):
   - mySQL, SQL SERVER, ORACLE, mongoDB, postgres etc.
   - banco em memória (H2).
   - banco em container - mySQL, SQL SERVER, ORACLE, mongoDB, postgres etc.
5. Pode utilizar qualquer framework que esteja familiarizado:
   - Spring Boot.
   - Micronaut.
   - Quarkus.
   - Sem framework.
6. Utilizar o gerenciador de dependências de sua preferência (**Maven/Gradle**).
7. Utilizar um pattern de desenvolvimento que faça sentido, dado o contexto.
   - Referência de patterns: [Refactoring Guru](https://refactoring.guru/)
8. Versionar seu código em algum controlador de versão (**GitHub**, CVS etc).
9. Descrever/demonstrar quais práticas/fatores da metodologia **12 Factor App** foram utilizados na sua solução.
   - Referência: [12 Factor App](https://12factor.net/pt_br/)
