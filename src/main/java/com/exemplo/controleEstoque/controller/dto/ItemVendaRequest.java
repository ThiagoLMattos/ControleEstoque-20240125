package com.exemplo.controleEstoque.controller.dto;

import java.math.BigDecimal;

public class ItemVendaRequest {
    
    private Long produtoId;
    private Integer quantidade;
    private BigDecimal descontoPercentual;

    public ItemVendaRequest() {}

    public ItemVendaRequest(Long produtoId, Integer quantidade, BigDecimal descontoPercentual) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.descontoPercentual = descontoPercentual;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getDescontoPercentual() {
        return descontoPercentual;
    }

    public void setDescontoPercentual(BigDecimal descontoPercentual) {
        this.descontoPercentual = descontoPercentual;
    }
}