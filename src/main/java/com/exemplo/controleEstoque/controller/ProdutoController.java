package com.exemplo.controleEstoque.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.exemplo.controleEstoque.model.Fornecedor;
import com.exemplo.controleEstoque.model.Produto;
import com.exemplo.controleEstoque.repository.CategoriaRepository;
import com.exemplo.controleEstoque.repository.FornecedorRepository;
import com.exemplo.controleEstoque.repository.ProdutoRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {
    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final FornecedorRepository fornecedorRepository;

    @GetMapping
    public List<Produto> getAllProdutos() {
        return produtoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getCategoriaById(@PathVariable Long id) {
        return produtoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Produto> createProduto(@RequestBody Produto produto) {

        // 1. Vincula o Produto ao Estoque
        if (produto.getEstoque() != null) {
            
            produto.getEstoque().setProduto(produto);
        }

        // 2. Verifica e busca a Categoria
        if (produto.getCategoria() == null || produto.getCategoria().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        categoriaRepository.findById(produto.getCategoria().getId())
                .ifPresent(produto::setCategoria);

        // 3. Atualiza fornecedores corretamente
        if (produto.getFornecedores() != null && !produto.getFornecedores().isEmpty()) {
            Set<Fornecedor> fornecedoresValidos = new HashSet<>();
            for (Fornecedor fornecedor : produto.getFornecedores()) {
                fornecedorRepository.findById(fornecedor.getId())
                        .ifPresent(fornecedoresValidos::add);
            }
            produto.setFornecedores(fornecedoresValidos);
        }

        Produto savedProduto = produtoRepository.save(produto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> updateProduto(
            @PathVariable Long id, @RequestBody Produto produtoDetails) {
        return produtoRepository.findById(id)
                .map(produto -> {
                    produto.setNome(produtoDetails.getNome());
                    Produto updatedProduto = produtoRepository.save(produto);
                    return ResponseEntity.ok(updatedProduto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        if (!produtoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        produtoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
