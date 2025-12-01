package com.exemplo.controleEstoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exemplo.controleEstoque.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>{
    
}
