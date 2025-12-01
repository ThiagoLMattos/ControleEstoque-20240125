package com.exemplo.controleEstoque.controller;

import java.util.List;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.exemplo.controleEstoque.model.Fornecedor;
import com.exemplo.controleEstoque.repository.FornecedorRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {
    private final FornecedorRepository fornecedorRepository;

    @GetMapping
    public List<Fornecedor> getAllFornecedores() {
        return fornecedorRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fornecedor> getFornecedorById(@PathVariable Long id) {
        return fornecedorRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Fornecedor createFornecedor(@RequestBody Fornecedor fornecedor) {
        return fornecedorRepository.save(fornecedor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fornecedor> updateFornecedor(
        @PathVariable Long id, @RequestBody Fornecedor fornecedorDetails
    ){
        return fornecedorRepository.findById(id)
            .map(fornecedor -> {
                fornecedor.setNome(fornecedorDetails.getNome());
                Fornecedor updatedFornecedor = fornecedorRepository.save(fornecedor);
                return ResponseEntity.ok(updatedFornecedor);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFornecedor(@PathVariable Long id) {
        if(!fornecedorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        fornecedorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
