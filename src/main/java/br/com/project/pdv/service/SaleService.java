package br.com.project.pdv.service;

import br.com.project.pdv.dto.ProductSaleDTO;
import br.com.project.pdv.dto.ProductInfoDTO;
import br.com.project.pdv.dto.SaleDTO;
import br.com.project.pdv.dto.SaleInfoDTO;
import br.com.project.pdv.entity.ItemSaleEntity;
import br.com.project.pdv.entity.ProductEntity;
import br.com.project.pdv.entity.SaleEntity;
import br.com.project.pdv.entity.UserEntity;
import br.com.project.pdv.exceptions.InvalidOperationException;
import br.com.project.pdv.exceptions.NoItemException;
import br.com.project.pdv.repository.ItemSaleRepository;
import br.com.project.pdv.repository.ProductRepository;
import br.com.project.pdv.repository.SaleRepository;
import br.com.project.pdv.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;
    private final ItemSaleRepository itemSaleRepository;

    public List<SaleInfoDTO> findAll() {
        return saleRepository.findAll().stream().map(sale -> getSaleInfo(sale)).collect(Collectors.toList());
    }

    private SaleInfoDTO getSaleInfo(SaleEntity sale) {
       var products = getProductInfo(sale.getItems());
        BigDecimal total = getTotal(products);


        return SaleInfoDTO.builder()
                .user(sale.getUser().getName())
                .date(sale.getSale_date().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .productInfoDTOS(products)
                .total(total)
                .build();
    }

    private BigDecimal getTotal(List<ProductInfoDTO> products) {
        BigDecimal total = new BigDecimal(0);
        for (int i=0; i < products.size(); i++){
            ProductInfoDTO currentProduct = products.get(i);
            total = total.add(currentProduct.getPrice()
                    .multiply(new BigDecimal(currentProduct.getQuantity())));
        }
        return total;
    }

    private List<ProductInfoDTO> getProductInfo(List<ItemSaleEntity> itemSaleEntityList) {
        if (CollectionUtils.isEmpty(itemSaleEntityList)) {
            return Collections.emptyList();
        }
        return itemSaleEntityList.stream().map(
                itemSaleEntity -> ProductInfoDTO
                        .builder()
                        .id(itemSaleEntity.getId())
                        .price(itemSaleEntity.getProduct().getPrice())
                        .description(itemSaleEntity.getProduct().getDescription())
                        .quantity(itemSaleEntity.getQuantity())
                        .build()
        ).collect(Collectors.toList());
    }


    @Transactional
    public long save(SaleDTO saleDTO) {
        UserEntity userEntity = userRepository.findById(saleDTO.getUserid())
                .orElseThrow(() -> new NoItemException("Usúario não encontrado."));

        SaleEntity newSale = new SaleEntity();
        newSale.setUser(userEntity);
        newSale.setSale_date(LocalDate.now());
        List<ItemSaleEntity> itemSaleEntityList = getItemSale(saleDTO.getItems());

        newSale = saleRepository.save(newSale);
        saveItemSale(itemSaleEntityList, newSale);
        return newSale.getId();
    }

    private void saveItemSale(List<ItemSaleEntity> itemSaleEntityList, SaleEntity newSale) {
        for (ItemSaleEntity item : itemSaleEntityList) {
            item.setSale(newSale);
            itemSaleRepository.save(item);


        }
    }

    private List<ItemSaleEntity> getItemSale(List<ProductSaleDTO> productDTO) {

        if (productDTO.isEmpty()) {
            throw new InvalidOperationException("Não é possível adicionar a venda sem itens!");
        }


        return productDTO.stream().map(item -> {
            ProductEntity productEntity = productRepository.findById((item.getProductid()))
                    .orElseThrow(() -> new NoItemException("Item da venda não encontrado."));

            ItemSaleEntity itemSaleEntity = new ItemSaleEntity();
            itemSaleEntity.setProduct(productEntity);
            itemSaleEntity.setQuantity(item.getQuantity());

            if (productEntity.getQuantity() == 0) {
                throw new NoItemException("Produto sem estoque.");
            } else if (productEntity.getQuantity() < item.getQuantity()) {
                throw new InvalidOperationException("A quantidade em estoque é menor, do que a venda desejada.");
            }

            int total = productEntity.getQuantity() - item.getQuantity();
            productEntity.setQuantity(total);
            productRepository.save(productEntity);

            return itemSaleEntity;
        }).collect(Collectors.toList());
    }

    public SaleInfoDTO getById(long id) {
        SaleEntity saleEntity = saleRepository.findById(id)
                .orElseThrow(() -> new NoItemException("Venda não encontrada!"));
        return getSaleInfo(saleEntity);
    }
}
