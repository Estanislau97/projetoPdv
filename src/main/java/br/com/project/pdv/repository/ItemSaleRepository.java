package br.com.project.pdv.repository;

import br.com.project.pdv.entity.ItemSaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


//Responsável por manipular o meu banco de dados
@Repository
public interface ItemSaleRepository extends JpaRepository<ItemSaleEntity, Long> {

}
