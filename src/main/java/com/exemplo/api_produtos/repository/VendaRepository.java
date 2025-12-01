package com.exemplo.api_produtos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.exemplo.api_produtos.model.Venda;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long>{
    
}
