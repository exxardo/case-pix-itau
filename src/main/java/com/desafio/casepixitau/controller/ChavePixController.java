package com.desafio.casepixitau.controller;

import com.desafio.casepixitau.dto.*;
import com.desafio.casepixitau.exception.ChavePixException;
import com.desafio.casepixitau.service.ChavePixService;
import com.desafio.casepixitau.util.HttpStatusCodes;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Controlador para operações relacionadas a chaves PIX.
 */
@RestController
@RequestMapping("/api/pix")
public class ChavePixController {

    private final ChavePixService service;

    /**
     * Construtor do controlador, injeta a dependência do serviço.
     *
     * @param service Serviço responsável pelas operações da chave PIX.
     */
    public ChavePixController(ChavePixService service) {
        this.service = service;
    }

    /**
     * Endpoint para inclusão de uma nova chave PIX.
     *
     * @param dto Objeto contendo os dados da chave a ser cadastrada.
     * @return ResponseEntity contendo a chave cadastrada ou um erro de validação.
     */
    @PostMapping
    public ResponseEntity<?> incluir(@Valid @RequestBody ChavePixRequestDTO dto) {
        try {
            ChavePixResponseDTO response = service.incluir(dto);
            return ResponseEntity.status(HttpStatusCodes.SUCCESS).body(response);
        } catch (ChavePixException e) {
            return ResponseEntity.status(HttpStatusCodes.UNPROCESSABLE_ENTITY)
                    .body(new ErrorResponseDTO(e.getMessage()));
        }
    }

    /**
     * Endpoint para alteração de uma chave PIX existente.
     *
     * @param id  Identificador da chave a ser alterada.
     * @param dto Objeto contendo os novos dados da chave.
     * @return ResponseEntity com a chave alterada ou erro correspondente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> alterar(@PathVariable UUID id,
                                     @Valid @RequestBody ChavePixAlteracaoDTO dto) {
        try {
            ChavePixResponseDTO response = service.alterar(id, dto);
            return ResponseEntity.status(HttpStatusCodes.SUCCESS).body(response);
        } catch (ChavePixException e) {
            if (e.getMessage().contains("não encontrada")) {
                return ResponseEntity.status(HttpStatusCodes.NOT_FOUND)
                        .body(new ErrorResponseDTO(e.getMessage()));
            } else {
                return ResponseEntity.status(HttpStatusCodes.UNPROCESSABLE_ENTITY)
                        .body(new ErrorResponseDTO(e.getMessage()));
            }
        }
    }

    /**
     * Endpoint para inativação de uma chave PIX.
     *
     * @param id Identificador da chave a ser inativada.
     * @return ResponseEntity contendo os dados da chave inativada ou erro correspondente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> inativar(@PathVariable UUID id) {
        try {
            ChavePixResponseDTO response = service.inativar(id);
            return ResponseEntity.status(HttpStatusCodes.SUCCESS).body(response);
        } catch (ChavePixException e) {
            return ResponseEntity.status(HttpStatusCodes.UNPROCESSABLE_ENTITY)
                    .body(new ErrorResponseDTO(e.getMessage()));
        }
    }

    /**
     * Consulta uma chave PIX pelo identificador único (UUID). Se o ID for informado,
     * nenhum outro filtro pode ser aceito. Caso contrário, retorna um erro 422.
     *
     * Retorna 200 se a chave for encontrada, 404 se não for encontrada,
     * e 422 se filtros adicionais forem passados junto com o ID.
     */
    @GetMapping("/filtros/{id}")
    public ResponseEntity<?> consultarPorId(
            @PathVariable("id") UUID id,
            @RequestParam(value = "tipo", required = false) String tipoChave,
            @RequestParam(value = "valor", required = false) String valorChave,
            @RequestParam(value = "agencia", required = false) Integer agencia,
            @RequestParam(value = "conta", required = false) Integer conta,
            @RequestParam(value = "dataInclusao", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInclusao,
            @RequestParam(value = "dataInativacao", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInativacao) {

        // Regra: Se o ID for informado, nenhum outro filtro pode ser aceito.
        if (tipoChave != null || valorChave != null || agencia != null || conta != null || dataInclusao != null || dataInativacao != null) {
            return ResponseEntity.status(HttpStatusCodes.UNPROCESSABLE_ENTITY)
                    .body(new ErrorResponseDTO("Se o ID for informado, nenhum outro filtro pode ser aceito."));
        }

        try {
            ChavePixResponseDTO response = service.consultarPorId(id);
            return ResponseEntity.status(HttpStatusCodes.SUCCESS).body(response);
        } catch (ChavePixException e) {
            return ResponseEntity.status(HttpStatusCodes.NOT_FOUND)
                    .body(new ErrorResponseDTO(e.getMessage()));
        }
    }

    /**
     * Consulta chaves PIX com base em filtros opcionais. Pelo menos um filtro deve ser informado,
     * e não é permitido combinar filtros de data de inclusão e data de inativação ao mesmo tempo.
     *
     * @param tipoChave O tipo da chave PIX (opcional).
     * @param valorChave O valor da chave PIX (opcional).
     * @param agencia O número da agência bancária (opcional).
     * @param conta O número da conta bancária (opcional).
     * @param dataInclusao A data de inclusão da chave PIX (não pode ser combinada com dataInativacao).
     * @param dataInativacao A data de inativação da chave PIX (não pode ser combinada com dataInclusao).
     * @return {@code ResponseEntity} contendo a lista de chaves encontradas ou um erro adequado.
     *         Retorna 200 se houver registros, 404 se nenhum registro for encontrado,
     *         e 422 se nenhuma chave for informada ou se ambas as datas forem passadas juntas.
     */
    @GetMapping("/filtros")
    public ResponseEntity<?> consultarPorFiltros(
            @RequestParam(value = "tipo", required = false) String tipoChave,
            @RequestParam(value = "valor", required = false) String valorChave,
            @RequestParam(value = "agencia", required = false) Integer agencia,
            @RequestParam(value = "conta", required = false) Integer conta,
            @RequestParam(value = "dataInclusao", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInclusao,
            @RequestParam(value = "dataInativacao", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInativacao) {

        // Regra 1: Se nenhum filtro for informado, retorna 422
        if (tipoChave == null && valorChave == null && agencia == null && conta == null && dataInclusao == null && dataInativacao == null) {
            return ResponseEntity.status(HttpStatusCodes.UNPROCESSABLE_ENTITY)
                    .body(new ErrorResponseDTO("Ao menos um filtro deve ser informado."));
        }

        // Regra 2: Se ambas as datas forem informadas, retorna 422
        if (dataInclusao != null && dataInativacao != null) {
            return ResponseEntity.status(HttpStatusCodes.UNPROCESSABLE_ENTITY)
                    .body(new ErrorResponseDTO("Não é permitido informar ambas as datas ao mesmo tempo."));
        }

        List<ChavePixResponseDTO> response = service.consultarPorFiltros(
                tipoChave, valorChave, agencia, conta, dataInclusao, dataInativacao
        );

        // Regra 3: Se nenhum registro for encontrado, retorna 404
        if (response.isEmpty()) {
            return ResponseEntity.status(HttpStatusCodes.NOT_FOUND)
                    .body(new ErrorResponseDTO("Nenhum registro encontrado para os filtros informados."));
        }

        return ResponseEntity.status(HttpStatusCodes.SUCCESS).body(response);
    }
}
