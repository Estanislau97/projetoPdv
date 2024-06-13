package br.com.project.pdv.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_db")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //identificador unico da tabela
    private Long id;

    @Column(length = 100, nullable = false) //tamanho do nome, esse campo não pode ser nulo
    @NotBlank(message = "Campo nome é obrigatório.")
    private String name;

    private boolean isEnabled; //Para saber se o usuário é ativo ou não no sistema.

    @OneToMany(mappedBy = "user")//Mapeando com "user" do, private User "user";
    private List<SaleEntity> sales; //A partir do meu usuário eu quero ter acesso as vendas dele

}
