package com.exemplo.controleEstoque.controller.dto;

import java.util.Date;
import java.util.List;

public class VendaRequest {
    
    private Long clienteId;
    private Date dataVenda;
    private List<ItemVendaRequest> itens;

    public VendaRequest() {}

    public VendaRequest(Long clienteId, Date dataVenda, List<ItemVendaRequest> itens) {
        this.clienteId = clienteId;
        this.dataVenda = dataVenda;
        this.itens = itens;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public List<ItemVendaRequest> getItens() {
        return itens;
    }

    public void setItens(List<ItemVendaRequest> itens) {
        this.itens = itens;
    }
}