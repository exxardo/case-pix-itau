# CASE - Desenvolvimento módulo de cadastro de chaves PIX

## Introdução

O Pix é o arranjo de pagamentos e recebimentos instantâneos, disponível todos os dias do ano, com liquidação em tempo real de suas transações. Ou seja, permite a transferência imediata de valores entre diferentes instituições, em todos os horários e dias, entre pessoas físicas, empresas ou entes governamentais.

O Pix é uma forma de pagar e receber valores.  
**Chave Pix**: um apelido para a conta transacional que deve ser atribuído pelo titular da conta ou representante legal/operador permissionado, usado para identificar a conta corrente do cliente recebedor por meio de uma única informação. Essa chave retornará a informação que identificará o recebedor da cobrança.

### Formatos das chaves Pix:
- **Número de celular**: Inicia-se com "+", seguido do código do país, DDD e número com nove dígitos.
- **E-mail**: Contém "@", tamanho máximo de 77 caracteres.
- **CPF**: Utilizado com 11 dígitos (sem pontos ou traços).
- **CNPJ**: Utilizado com 14 dígitos (sem pontos ou traços).
- **Chave Aleatória**: Informação alfanumérica com 36 posições (sem pontuação).

### Regras gerais:
- Cadastro limitado a até 5 chaves por conta para Pessoa Física e até 20 chaves por conta para Pessoa Jurídica.
- As chaves cadastradas devem ser armazenadas e disponibilizadas aos correntistas para consulta.

---

## Inclusão

### Objetivo
Viabilizar a inclusão de chaves PIX, vinculando a chave à agência e conta do correntista Itaú.  
A implementação deve contemplar no mínimo três dos cinco tipos de chaves (ex.: celular, e-mail e CPF).

### Dados de entrada (Tabela 1)

| Nome                  | Tipo Dado       | Obrigatório |
|-----------------------|-----------------|-------------|
| TIPO CHAVE            | Texto (9)      | SIM         |
| VALOR CHAVE           | Texto (77)     | SIM         |
| TIPO CONTA            | Texto (10)     | SIM         |
| NUMERO AGENCIA        | Numérico (4)   | SIM         |
| NUMERO CONTA          | Numérico (8)   | SIM         |
| NOME CORRENTISTA      | Texto (30)     | SIM         |
| SOBRENOME CORRENTISTA | Texto (45)     | NÃO         |

### Critérios de aceite
1. Registrar as informações no banco de dados.
2. Gerar um código único de registro no formato UUID.
3. Limitar o número máximo de chaves por conta:
    - 5 para Pessoa Física.
    - 20 para Pessoa Jurídica.
4. Não permitir duplicidade no campo "VALOR CHAVE".
5. Registrar data e hora da inclusão da chave.
6. Validar as regras específicas para cada tipo de chave:
    - **Celular**: Validar código do país (+), DDD e número com nove dígitos.
    - **E-mail**: Deve conter "@", ser alfanumérico e ter no máximo 77 caracteres.
    - **CPF/CNPJ**: Validar formato, número máximo de dígitos e validade.
    - **Chave Aleatória**: Aceitar até 36 caracteres alfanuméricos.
7. Respeitar obrigatoriedade dos campos conforme Tabela 1.
8. Retornar HTTP code:
    - `200` em caso de sucesso (retorna ID gerado).
    - `422` se houver erro nas validações.

---

## Alteração

### Objetivo
Permitir alteração dos dados associados a uma chave registrada (exceto ID, tipo ou valor da chave).

### Dados de entrada (Tabela 2)

| Nome                  | Tipo Dado       | Obrigatório |
|-----------------------|-----------------|-------------|
| ID                    | UUID           | SIM         |
| TIPO CONTA            | Texto (10)     | SIM         |
| NUMERO AGENCIA        | Numérico (4)   | SIM         |
| NUMERO CONTA          | Numérico (8)   | SIM         |
| NOME CORRENTISTA      | Texto (30)     | SIM         |
| SOBRENOME CORRENTISTA | Texto (45)     | NÃO         |

### Dados de saída (Tabela 3)

| Nome                  | Tipo Dado       |
|-----------------------|-----------------|
| ID                    | UUID           |
| TIPO CHAVE            | Texto (9)      |
| VALOR CHAVE           | Texto (77)     |
| TIPO CONTA            | Texto (10)     |
| NUMERO AGENCIA        | Numérico (4)   |
| NUMERO CONTA          | Numérico (8)   |
| NOME CORRENTISTA      | Texto (30)     |
| SOBRENOME CORRENTISTA | Texto (45)     |
| DATA HORA INCLUSAO    | DATETIME       |

### Critérios de aceite
1. Validar as alterações conforme regras específicas do tipo de dado.
2. Não permitir alterações nos campos ID, tipo ou valor da chave.
3. Não permitir alterações em chaves inativadas.
4. Retornar HTTP code:
    - `200` em caso de sucesso.
    - `422` se houver erro nas validações.
    - `404` se o ID não for encontrado.

---

## Deleção

### Objetivo
Inativar uma chave registrada por ID, impedindo alterações ou consultas futuras.

### Dados de saída (Tabela 4)

Mesmo formato da Tabela 3, adicionando:
- DATA HORA INATIVAÇÃO DA CHAVE.

### Critérios de aceite
1. Retornar erro (`422`) se a chave já estiver desativada.
2. Registrar data/hora da inativação.
3. Retornar HTTP code:
    - `200` em caso de sucesso.

---

## Consulta

### Objetivo
Disponibilizar consultas por:
- ID (**obrigatório implementar**).
- Tipo de chave, agência/conta, nome do correntista, data de inclusão ou inativação.

### Dados de saída (Tabela 5)

Mesmo formato da Tabela 3, adicionando:
- DATA HORA INATIVAÇÃO DA CHAVE.

### Critérios de aceite
1. Permitir combinações entre filtros exceto entre data de inclusão/inativação.
2. Retornar erro (`422`) se filtros inválidos forem combinados.
3. Retornar erro (`404`) se nenhuma chave for encontrada.
4. Campos nulos devem ser apresentados como vazio (`""`).

---

## Considerações finais

O objetivo é desenvolver APIs funcionais para cadastro, alteração, deleção e consulta das chaves PIX utilizando boas práticas como testes unitários (\(90\%\)), banco de dados à escolha e frameworks como Spring Boot ou Quarkus. O código deve seguir princípios da metodologia *12 Factor App* para garantir escalabilidade e manutenibilidade.
