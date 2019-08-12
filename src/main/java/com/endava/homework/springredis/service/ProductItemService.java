package com.endava.homework.springredis.service;

import com.endava.homework.springredis.model.ProductItem;

import java.util.List;
import java.util.Set;

public interface ProductItemService {

    void saveAll(List<ProductItem> productItems);

    Set<Long> getAllPromotionIds();

    void deleteAll();
}
