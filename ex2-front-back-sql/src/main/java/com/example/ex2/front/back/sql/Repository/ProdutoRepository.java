package com.example.ex2.front.back.sql.Repository;

import com.example.ex2.front.back.sql.Model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT p FROM Produto p WHERE p.id = :id")
    Optional<Produto> findByIdWithLock(@Param("id") Long id);

    Page<Produto> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    @Query("SELECT SUM(p.preco * p.quantidade) FROM Produto p")
    Optional<BigDecimal> calcularValorTotalEstoque();
}
