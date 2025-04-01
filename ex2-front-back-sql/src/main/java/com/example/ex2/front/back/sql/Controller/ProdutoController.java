package com.example.ex2.front.back.sql.Controller;

import com.example.ex2.front.back.sql.DTO.ProdutoDTO;
import com.example.ex2.front.back.sql.Service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "API para gerenciamento de produtos no estoque")
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    @Operation(summary = "Criar novo produto")
    public ResponseEntity<ProdutoDTO> criar(@Valid @RequestBody ProdutoDTO produtoDTO) {
        ProdutoDTO produtoCriado = produtoService.criar(produtoDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(produtoCriado.id())
                .toUri();
        return ResponseEntity.created(location).body(produtoCriado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        ProdutoDTO produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produto);
    }

    @GetMapping
    @Operation(summary = "Listar produtos paginados")
    public ResponseEntity<Page<ProdutoDTO>> listarTodos(
            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        Page<ProdutoDTO> produtos = produtoService.listarTodos(pageable);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/valor-total")
    @Operation(summary = "Calcular valor total do estoque")
    public ResponseEntity<ValorEstoqueDTO> calcularValorTotal() {
        double valorTotal = produtoService.calcularValorTotalEstoque();
        ValorEstoqueDTO valorEstoqueDTO = new ValorEstoqueDTO(valorTotal);
        return ResponseEntity.ok(valorEstoqueDTO);
    }
}
