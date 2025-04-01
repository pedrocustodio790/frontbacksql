    package com.example.ex2.front.back.sql.DTO;

    import com.fasterxml.jackson.annotation.JsonInclude;
    import io.swagger.v3.oas.annotations.media.Schema;
    import jakarta.validation.constraints.*;

    import java.math.BigDecimal;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ProdutoDTO(
            @Schema(description = "ID do produto", example = "1")
            Long id,

            @Schema(description = "Nome do produto", example = "Notebook Dell")
            @NotBlank(message = "{produto.nome.obrigatorio}")
            @Size(max = 100, message = "{produto.nome.tamanho}")
            String nome,

            @Schema(description = "Preço unitário", example = "4599.99")
            @NotNull(message = "{produto.preco.obrigatorio}")
            @DecimalMin(value = "0.01", message = "{produto.preco.minimo}")
            @Digits(integer = 8, fraction = 2, message = "{produto.preco.formato}")
            BigDecimal preco,

            @Schema(description = "Quantidade em estoque", example = "10")
            @NotNull(message = "{produto.quantidade.obrigatorio}")
            @Min(value = 0, message = "{produto.quantidade.minima}")
            Integer quantidade
    ) {
        public ProdutoDTO withId(Long id) {
            return new ProdutoDTO(id, this.nome, this.preco, this.quantidade);
        }
    }
