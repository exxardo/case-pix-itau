package com.desafio.casepixitau.service;

import com.desafio.casepixitau.exception.ChavePixException;
import com.desafio.casepixitau.model.ChavePix;
import com.desafio.casepixitau.repository.ChavePixRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Serviço responsável pelo gerenciamento das operações relacionadas às chaves Pix.
 */
@Service
public class ChavePixService {

    private final ChavePixRepository repository;

    /**
     * Construtor do serviço que recebe uma instância do repositório.
     *
     * @param repository Repositório responsável pela persistência das chaves Pix.
     */
    public ChavePixService(ChavePixRepository repository) {
        this.repository = repository;
    }

    /**
     * Cadastra uma nova chave Pix no sistema.
     *
     * @param chavePix Objeto contendo os dados da chave Pix a ser cadastrada.
     * @return A chave Pix cadastrada.
     * @throws ChavePixException Se a chave já estiver cadastrada.
     */
    public ChavePix cadastrar(ChavePix chavePix) {
        if (repository.findByValorChave(chavePix.getValorChave()).isPresent()) {
            throw new ChavePixException("Chave Pix já cadastrada.");
        }
        chavePix.setDataHoraInclusao(LocalDateTime.now());
        return repository.save(chavePix);
    }

    /**
     * Consulta uma chave Pix pelo seu ID.
     *
     * @param id Identificador único da chave Pix.
     * @return A chave Pix encontrada.
     * @throws ChavePixException Se a chave não for encontrada.
     */
    public ChavePix consultar(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ChavePixException("Chave Pix não encontrada."));
    }

    /**
     * Lista todas as chaves Pix cadastradas no sistema.
     *
     * @return Lista contendo todas as chaves Pix.
     */
    public List<ChavePix> listar() {
        return repository.findAll();
    }

    /**
     * Inativa uma chave Pix pelo seu ID.
     *
     * @param id Identificador único da chave Pix a ser inativada.
     * @throws ChavePixException Se a chave já estiver inativa.
     */
    public void inativar(UUID id) {
        ChavePix chave = consultar(id);
        if (chave.getDataHoraInativacao() != null) {
            throw new ChavePixException("A chave já está inativa.");
        }
        chave.setDataHoraInativacao(LocalDateTime.now());
        repository.save(chave);
    }
}


