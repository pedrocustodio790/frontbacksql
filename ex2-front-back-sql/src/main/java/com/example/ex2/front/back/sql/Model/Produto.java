package com.example.ex2.front.back.sql.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "produtos", indexes = {
        @Index(name = "idx_produto_nome", columnList = "nome"),
        @Index(name = "idx_produto_preco", columnList = "preco")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@OptimisticLocking(type = OptimisticLockType.VERSION)
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produto_seq")
    @SequenceGenerator(name = "produto_seq", sequenceName = "produto_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Integer quantidade;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;

    @Transient
    public BigDecimal calcularValorTotal() {
        return preco.multiply(BigDecimal.valueOf(quantidade));
    }
}
