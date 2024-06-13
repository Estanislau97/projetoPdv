package br.com.project.pdv.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_db")
@Entity
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    @NotBlank(message = "O campo descrição é obrigatório!")
    private String description;

    @Column(length = 20, precision = 20, scale = 2, nullable = false)
    @NotNull(message = "O campo preço é obrigatório!")
    private BigDecimal price;

    @Column(nullable = true)
    @NotNull(message = "O campo quantidade é obrigatório!")
    private  int quantity;
}
