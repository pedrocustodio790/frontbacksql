package com.example.ex2.front.back.sql.Service;

import com.example.ex2.front.back.sql.DTO.ProdutoDTO;
import com.example.ex2.front.back.sql.Model.Produto;
import com.example.ex2.front.back.sql.Repository.ProdutoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    @Transactional(readOnly = true)
    @Cacheable(value = "produtos", key = "#id")
    public ProdutoDTO buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .map(produtoMapper::toDTO)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    @Transactional
    @CacheEvict(value = "produtos", allEntries = true)
    public ProdutoDTO criar(@Valid ProdutoDTO produtoDTO) {
        Produto produto = produtoMapper.toEntity(produtoDTO);
        Produto produtoSalvo = produtoRepository.save(produto);
        return produtoMapper.toDTO(produtoSalvo);
    }

    @Transactional
    @CacheEvict(value = "produtos", key = "#id")
    @Retryable(value = ObjectOptimisticLockingFailureException.class, maxAttempts = 3)
    public ProdutoDTO atualizar(Long id, ProdutoDTO produtoDTO) {
        Produto produtoExistente = produtoRepository.findByIdWithLock(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));

        produtoMapper.updateEntity(produtoDTO, produtoExistente);
        return produtoMapper.toDTO(produtoExistente);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoDTO> listarTodos(Pageable pageable) {
        return produtoRepository.findAll(pageable)
                .map(produtoMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularValorTotalEstoque() {
        return produtoRepository.calcularValorTotalEstoque()
                .orElse(BigDecimal.ZERO);
    }
}
