package br.com.project.pdv.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "item_sale_db")
@Entity
public class ItemSaleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "saleId_db", nullable = false) //Chave estrangeira de Sale
    private SaleEntity sale;


    @ManyToOne
    @JoinColumn(name = "productId_db", nullable = false)
    private ProductEntity product;


}
