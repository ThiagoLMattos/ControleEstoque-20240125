package com.exemplo.controleEstoque.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_clientes")
public class Cliente {    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @JsonIgnore 
    private List<Venda> vendas;

    public Cliente(){}

    public Cliente(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome( String nome ) { this.nome = nome; }
    public String getEmail () {return email; }
    public void setEmail (String email ) {this.email = email; }
    public List<Venda> getVendas () { return vendas; }
    public void setVendas (List<Venda> vendas) { this.vendas = vendas; }
}
