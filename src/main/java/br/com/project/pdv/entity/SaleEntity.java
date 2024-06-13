package br.com.project.pdv.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sale_db")
public class SaleEntity {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @Column(name = "sale_date_db", nullable = false)
     private LocalDate sale_date;

     @ManyToOne //Relacionamento muitas vendas para um usuário
     @JoinColumn(name = "userId-db") //chave estrangeira do user
     private UserEntity user;

     @OneToMany(mappedBy = "sale", fetch = FetchType.LAZY) //Quando eu for obter uma venda, eu não quero que venha os itens juntos
     private List<ItemSaleEntity> items;


}
