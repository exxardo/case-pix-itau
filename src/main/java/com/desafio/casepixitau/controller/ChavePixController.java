package com.desafio.casepixitau.controller;

import com.desafio.casepixitau.dto.ChavePixRequestDTO;
import com.desafio.casepixitau.dto.ChavePixResponseDTO;
import com.desafio.casepixitau.exception.ChavePixException;
import com.desafio.casepixitau.service.ChavePixService;
import com.desafio.casepixitau.util.HttpStatusCodes;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pix")
public class ChavePixController {

    private final ChavePixService service;

    public ChavePixController(ChavePixService service) {
        this.service = service;
    }

    /**
     * Endpoint para incluir uma nova chave PIX.
     *
     * @param dto Dados da chave PIX a ser cadastrada.
     * @return Resposta contendo o ID gerado para a chave cadastrada.
     */
    @PostMapping
    public ResponseEntity<ChavePixResponseDTO> incluir(@Valid @RequestBody ChavePixRequestDTO dto) {
        try {
            ChavePixResponseDTO response = service.incluir(dto);
            return ResponseEntity.status(HttpStatusCodes.SUCCESS).body(response);
        } catch (ChavePixException e) {
            return ResponseEntity.status(HttpStatusCodes.UNPROCESSABLE_ENTITY)
                    .body(null);
        }
    }

    /**
     * Endpoint para alterar os dados de uma chave PIX existente.
     *
     * @param id  UUID da chave a ser alterada.
     * @param dto Dados atualizados da chave PIX.
     * @return Resposta contendo os dados atualizados da chave.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChavePixResponseDTO> alterar(
            @PathVariable UUID id,
            @Valid @RequestBody ChavePixRequestDTO dto) {
        try {
            ChavePixResponseDTO response = service.alterar(id, dto);
            return ResponseEntity.status(HttpStatusCodes.SUCCESS).body(response);
        } catch (ChavePixException e) {
            return ResponseEntity.status(HttpStatusCodes.UNPROCESSABLE_ENTITY)
                    .body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCodes.NOT_FOUND).body(null);
        }
    }

    /**
     * Endpoint para inativar uma chave PIX por ID.
     *
     * @param id UUID da chave a ser inativada.
     * @return Resposta contendo os dados da chave inativada.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ChavePixResponseDTO> inativar(@PathVariable UUID id) {
        try {
            ChavePixResponseDTO response = service.inativar(id);
            return ResponseEntity.status(HttpStatusCodes.SUCCESS).body(response);  // Retorna com data de inativação
        } catch (ChavePixException e) {
            return ResponseEntity.status(HttpStatusCodes.UNPROCESSABLE_ENTITY).body(null);
        }
    }

    /**
     * Consulta uma chave PIX pelo ID.
     *
     * @param id UUID da chave PIX.
     * @return Resposta contendo os dados da chave encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChavePixResponseDTO> consultarPorId(@PathVariable UUID id) {
        try {
            ChavePixResponseDTO response = service.consultarPorId(id);
            return ResponseEntity.status(HttpStatusCodes.SUCCESS).body(response);
        } catch (ChavePixException e) {
            return ResponseEntity.status(HttpStatusCodes.NOT_FOUND).body(null);
        }
    }

    /**
     * Consulta chaves PIX pelo tipo de chave (celular, email ou CPF).
     *
     * @param tipoChave Tipo da chave (celular, email ou CPF).
     * @return Lista de chaves PIX que correspondem ao tipo informado.
     */
    @GetMapping("/tipo")
    public ResponseEntity<List<ChavePixResponseDTO>> consultarPorTipoChave(
            @RequestParam("tipo") String tipoChave) {
        List<ChavePixResponseDTO> response = service.consultarPorTipoChave(tipoChave);
        return ResponseEntity.status(HttpStatusCodes.SUCCESS).body(response);
    }

    /**
     * Consulta chaves PIX pela agência e conta.
     *
     * @param agencia Número da agência.
     * @param conta   Número da conta.
     * @return Lista de chaves PIX que correspondem à agência e conta informadas.
     */
    @GetMapping("/agencia-conta")
    public ResponseEntity<List<ChavePixResponseDTO>> consultarPorAgenciaEConta(
            @RequestParam("agencia") int agencia,
            @RequestParam("conta") int conta) {
        List<ChavePixResponseDTO> response = service.consultarPorAgenciaEConta(agencia, conta);
        return ResponseEntity.status(HttpStatusCodes.SUCCESS).body(response);
    }

    /**
     * Consulta chaves PIX por data de inclusão ou inativação (não combinadas).
     *
     * @param dataInclusao Data de inclusão da chave (opcional).
     * @param dataInativacao Data de inativação da chave (opcional).
     * @return Lista de chaves PIX que correspondem ao filtro informado.
     */
    @GetMapping("/data")
    public ResponseEntity<List<ChavePixResponseDTO>> consultarPorData(
            @RequestParam(value = "dataInclusao", required = false) LocalDate dataInclusao,
            @RequestParam(value = "dataInativacao", required = false) LocalDate dataInativacao) {
        if (dataInclusao != null && dataInativacao != null) {
            return ResponseEntity.status(HttpStatusCodes.UNPROCESSABLE_ENTITY)
                    .body(null); // Não é permitido combinar filtros de data de inclusão e inativação
        }
        List<ChavePixResponseDTO> response = service.consultarPorData(dataInclusao, dataInativacao);
        return ResponseEntity.status(HttpStatusCodes.SUCCESS).body(response);
    }

    /**
     * Consulta chaves PIX pelo nome do correntista.
     *
     * @param nomeCorrentista Nome do correntista.
     * @return Lista de chaves PIX que correspondem ao nome informado.
     */
    @GetMapping("/nome")
    public ResponseEntity<List<ChavePixResponseDTO>> consultarPorNomeCorrentista(
            @RequestParam("nome") String nomeCorrentista) {
        try {
            List<ChavePixResponseDTO> response = service.consultarPorNomeCorrentista(nomeCorrentista);
            return ResponseEntity.status(HttpStatusCodes.SUCCESS).body(response);
        } catch (ChavePixException e) {
            return ResponseEntity.status(HttpStatusCodes.NOT_FOUND).body(null);
        }
    }

}
