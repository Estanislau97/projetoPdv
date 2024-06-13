package br.com.project.pdv.controller;

import br.com.project.pdv.dto.ProductDTO;
import br.com.project.pdv.dto.ResponseDTO;
import br.com.project.pdv.entity.ProductEntity;
import br.com.project.pdv.repository.ProductRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")

public class ProductController {
    private ProductRepository productRepository;
    private ModelMapper mapper;
    public ProductController(@Autowired ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.mapper = new ModelMapper();
    }

    @GetMapping()
    public ResponseEntity getAll() {
        return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity post(@Valid @RequestBody ProductDTO productDTO) {
        try {
            return new ResponseEntity<>(productRepository.save(mapper.map(productDTO, ProductEntity.class)), HttpStatus.CREATED);
        } catch (Exception error) {
            return new ResponseEntity<>(new ResponseDTO(error.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PutMapping()
    public ResponseEntity put(@Valid @RequestBody ProductDTO productDTO) {
        try {
            return new ResponseEntity<>(productRepository.save(mapper.map(productDTO, ProductEntity.class)), HttpStatus.OK);
        } catch (Exception error) {
            return new ResponseEntity<>(new ResponseDTO(error.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable long id) {
        try {
            productRepository.deleteById(id);
            return new ResponseEntity<>(new ResponseDTO("Produto removido com sucesso!"), HttpStatus.OK);
        } catch (Exception error) {
            return new ResponseEntity<>(new ResponseDTO(error.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}