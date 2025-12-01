package com.exemplo.api_produtos.model;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_produto_venda")
public class ProdutoVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;

    private Integer quantidade;

    private BigDecimal precoUnitario;

    public ProdutoVenda() {}

    public ProdutoVenda(Produto produto, Venda venda, Integer quantidade, BigDecimal precoUnitario) {
        this.produto = produto;
        this.venda = venda;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public Long getId() { return id; }
    public void setId(Long id) {this.id= id;}
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    public Venda getVenda() { return venda; }
    public void setVenda(Venda venda) { this.venda = venda; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
}
