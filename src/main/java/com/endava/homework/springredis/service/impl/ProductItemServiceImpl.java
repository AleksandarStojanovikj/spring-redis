package com.endava.homework.springredis.service.impl;

import com.endava.homework.springredis.model.ProductItem;
import com.endava.homework.springredis.repository.ProductItemRepository;
import com.endava.homework.springredis.service.ProductItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductItemServiceImpl implements ProductItemService {

    private ProductItemRepository productItemRepository;

    @Autowired
    public ProductItemServiceImpl(ProductItemRepository productItemRepository) {
        this.productItemRepository = productItemRepository;
    }


    @Override
    public void saveAll(List<ProductItem> productItems) {
        this.productItemRepository.saveAll(productItems);
    }

    @Override
    public Set<Long> getAllPromotionIds() {
        return productItemRepository.findAll()
                .stream()
                .filter(productItem -> productItem.getPromotionId() != null)
                .map(ProductItem::getPromotionId)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteAll() {
        productItemRepository.deleteAll();
    }
}
