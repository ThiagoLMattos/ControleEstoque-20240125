package com.exemplo.controleEstoque.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_vendas")
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date data;

    private BigDecimal preco;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    @JsonIgnore 
    private Set<ProdutoVenda> produtosVendidos;

    public Venda() {}

    public Venda(Cliente cliente, BigDecimal preco){
        this.cliente = cliente;
        this.preco = preco;
        this.data = new Date();
    }

    public Long getId() { return id; }
    public void setId(Long id) {this.id= id;}
    public BigDecimal getPreco() {return preco;}
    public void setPreco(BigDecimal preco) {this.preco = preco;}
    public Cliente getCliente() { return cliente;}
    public void setCliente(Cliente cliente) { this.cliente = cliente;}
    public Date getData () {return data; }
    public void setData (Date data){ this.data = data; }
    public Set<ProdutoVenda> getProdutosVendidos () {return produtosVendidos; }
    public void setProdutosVendidos (Set<ProdutoVenda> produtosVendidos ) {this.produtosVendidos = produtosVendidos; }
}
