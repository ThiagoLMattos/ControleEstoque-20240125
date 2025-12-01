package com.exemplo.controleEstoque.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.exemplo.controleEstoque.controller.dto.VendaRequest;
import com.exemplo.controleEstoque.controller.dto.ItemVendaRequest;
import com.exemplo.controleEstoque.controller.dto.ErrorResponse;
import com.exemplo.controleEstoque.model.*;
import com.exemplo.controleEstoque.repository.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
public class VendaController {
    
    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    @GetMapping
    public ResponseEntity<List<Venda>> listarTodasVendas() {
        List<Venda> vendas = vendaRepository.findAll();
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarVendaPorId(@PathVariable Long id) {
        Optional<Venda> venda = vendaRepository.findById(id);
        
        if (venda.isEmpty()) {
            ErrorResponse error = new ErrorResponse(
                404,
                "Venda Não Encontrada",
                "Não existe venda cadastrada com ID: " + id
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        return ResponseEntity.ok(venda.get());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> registrarVenda(@RequestBody VendaRequest request) {
        
        // 1. VALIDAÇÃO: Verificar se cliente existe
        Optional<Cliente> clienteOpt = clienteRepository.findById(request.getClienteId());
        if (clienteOpt.isEmpty()) {
            ErrorResponse error = new ErrorResponse(
                404,
                "Cliente Não Encontrado",
                "Não existe cliente cadastrado com ID: " + request.getClienteId()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        Cliente cliente = clienteOpt.get();

        // 2. VALIDAÇÃO: Verificar se a lista de itens não está vazia
        if (request.getItens() == null || request.getItens().isEmpty()) {
            ErrorResponse error = new ErrorResponse(
                400,
                "Requisição Inválida",
                "A venda deve conter pelo menos um item"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        // 3. VALIDAÇÃO: Verificar todos os produtos e estoques ANTES de processar
        List<ValidacaoEstoque> validacoes = new ArrayList<>();
        
        for (ItemVendaRequest item : request.getItens()) {
            // Validar quantidade
            if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                ErrorResponse error = new ErrorResponse(
                    400,
                    "Requisição Inválida",
                    "A quantidade do produto ID " + item.getProdutoId() + " deve ser maior que zero"
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            // Verificar se produto existe
            Optional<Produto> produtoOpt = produtoRepository.findById(item.getProdutoId());
            if (produtoOpt.isEmpty()) {
                ErrorResponse error = new ErrorResponse(
                    404,
                    "Produto Não Encontrado",
                    "Não existe produto cadastrado com ID: " + item.getProdutoId()
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            Produto produto = produtoOpt.get();

            // Verificar se tem estoque cadastrado
            if (produto.getEstoque() == null) {
                ErrorResponse error = new ErrorResponse(
                    400,
                    "Estoque Não Cadastrado",
                    "O produto '" + produto.getNome() + "' não possui estoque cadastrado"
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            // Verificar estoque suficiente
            int estoqueDisponivel = produto.getEstoque().getQuantidade();
            int quantidadeSolicitada = item.getQuantidade();

            if (estoqueDisponivel < quantidadeSolicitada) {
                ErrorResponse error = new ErrorResponse(
                    400,
                    "Estoque Insuficiente",
                    String.format("Estoque insuficiente para o produto '%s'. Disponível: %d, Solicitado: %d",
                        produto.getNome(), estoqueDisponivel, quantidadeSolicitada)
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            // Armazenar dados validados para processar depois
            validacoes.add(new ValidacaoEstoque(produto, item.getQuantidade(), item.getDescontoPercentual()));
        }

        // 4. PROCESSAMENTO: Criar a venda (todas validações passaram)
        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setData(request.getDataVenda() != null ? request.getDataVenda() : new Date());
        
        BigDecimal valorTotal = BigDecimal.ZERO;
        Set<ProdutoVenda> produtosVendidos = new HashSet<>();

        // 5. PROCESSAMENTO: Criar itens da venda e atualizar estoque
        for (ValidacaoEstoque validacao : validacoes) {
            Produto produto = validacao.getProduto();
            Integer quantidade = validacao.getQuantidade();
            BigDecimal descontoPercentual = validacao.getDescontoPercentual();

            // Calcular preço com desconto
            BigDecimal precoBase = produto.getPreco();
            BigDecimal precoFinal = precoBase;
            
            if (descontoPercentual != null && descontoPercentual.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal desconto = precoBase.multiply(descontoPercentual).divide(new BigDecimal("100"));
                precoFinal = precoBase.subtract(desconto);
            }

            // Criar item da venda
            ProdutoVenda produtoVenda = new ProdutoVenda();
            produtoVenda.setProduto(produto);
            produtoVenda.setVenda(venda);
            produtoVenda.setQuantidade(quantidade);
            produtoVenda.setPrecoUnitario(precoFinal);
            
            produtosVendidos.add(produtoVenda);

            // Atualizar estoque (dar baixa)
            Estoque estoque = produto.getEstoque();
            estoque.setQuantidade(estoque.getQuantidade() - quantidade);

            // Calcular valor total
            BigDecimal subtotal = precoFinal.multiply(BigDecimal.valueOf(quantidade));
            valorTotal = valorTotal.add(subtotal);
        }

        venda.setProdutosVendidos(produtosVendidos);
        venda.setPreco(valorTotal);

        // 6. PERSISTÊNCIA: Salvar tudo
        Venda vendaSalva = vendaRepository.save(venda);

        return ResponseEntity.status(HttpStatus.CREATED).body(vendaSalva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirVenda(@PathVariable Long id) {
        if (!vendaRepository.existsById(id)) {
            ErrorResponse error = new ErrorResponse(
                404,
                "Venda Não Encontrada",
                "Não existe venda cadastrada com ID: " + id
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        vendaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Classe auxiliar interna para armazenar validações
    private static class ValidacaoEstoque {
        private final Produto produto;
        private final Integer quantidade;
        private final BigDecimal descontoPercentual;

        public ValidacaoEstoque(Produto produto, Integer quantidade, BigDecimal descontoPercentual) {
            this.produto = produto;
            this.quantidade = quantidade;
            this.descontoPercentual = descontoPercentual;
        }

        public Produto getProduto() { return produto; }
        public Integer getQuantidade() { return quantidade; }
        public BigDecimal getDescontoPercentual() { return descontoPercentual; }
    }
}