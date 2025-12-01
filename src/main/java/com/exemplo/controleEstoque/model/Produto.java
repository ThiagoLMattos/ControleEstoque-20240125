package com.exemplo.controleEstoque.model;

import java.math.BigDecimal;
import java.util.Set;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_produtos")
public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private BigDecimal preco;

    @OneToOne(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Estoque estoque;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToMany
    @JoinTable(
        name = "tb_produto_fornecedor",
        joinColumns = @JoinColumn(name = "produto_id"),
        inverseJoinColumns = @JoinColumn(name = "fornecedor_id")        
    )
    private Set<Fornecedor> fornecedores;

    @OneToMany(mappedBy = "produto")
    private Set<ProdutoVenda> vendas;

    public Produto(){}

    public Produto (String nome, BigDecimal preco, Estoque estoque, Categoria categoria, Set<Fornecedor> fornecedores){
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
        this.categoria = categoria;
        this.fornecedores = fornecedores;
    }

    public Long getId() { return id; }
    public void setId(Long id) {this.id= id;}
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public BigDecimal getPreco() {return preco;}
    public void setPreco(BigDecimal preco) {this.preco = preco;}
    public Estoque getEstoque() {return estoque;}
    public void setEstoque(Estoque estoque) {this.estoque = estoque;}
    public Categoria getCategoria() { return categoria;}
    public void setCategoria(Categoria categoria) { this.categoria = categoria;}
    public Set<Fornecedor> getFornecedores() {return fornecedores; }
    public void setFornecedores (Set<Fornecedor> fornecedores) { this.fornecedores = fornecedores;}
    public Set<ProdutoVenda> getVendas() {return vendas; }
    public void setVendas (Set<ProdutoVenda> vendas) { this.vendas = vendas;}
}
