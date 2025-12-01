package com.exemplo.api_produtos.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.exemplo.api_produtos.model.Estoque;
import com.exemplo.api_produtos.model.ProdutoVenda;
import com.exemplo.api_produtos.model.Venda;
import com.exemplo.api_produtos.repository.VendaRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
public class VendaController {
    private final VendaRepository vendaRepository;

    @GetMapping
    public List<Venda> getAllVendas() {
        return vendaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> getVendaById(@PathVariable Long id) {
        return vendaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Venda createVenda(@RequestBody Venda venda) {
        BigDecimal total = BigDecimal.ZERO;

        for (ProdutoVenda produtoVenda : venda.getProdutosVendidos()) {
            produtoVenda.setVenda(venda);

            Estoque estoque = produtoVenda.getProduto().getEstoque();
            if (estoque.getQuantidade() < produtoVenda.getQuantidade()) {
                throw new IllegalArgumentException(
                        "Estoque insuficiente para o produto: " + produtoVenda.getProduto().getNome());
            }

            estoque.setQuantidade(estoque.getQuantidade() - produtoVenda.getQuantidade());

            total = total.add(
                    produtoVenda.getPrecoUnitario().multiply(
                            BigDecimal.valueOf(produtoVenda.getQuantidade())));
        }

        venda.setPreco(total);
        return vendaRepository.save(venda);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venda> updateVenda(
            @PathVariable Long id, @RequestBody Venda vendaDetails) {
        return vendaRepository.findById(id)
                .map(venda -> {
                    venda.setCliente(vendaDetails.getCliente());
                    venda.setProdutosVendidos(vendaDetails.getProdutosVendidos());
                    return ResponseEntity.ok(vendaRepository.save(venda));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenda(@PathVariable Long id) {
        if (!vendaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        vendaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
