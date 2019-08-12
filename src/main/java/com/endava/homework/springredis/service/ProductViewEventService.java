package com.endava.homework.springredis.service;

import com.endava.homework.springredis.model.ProductViewEvent;

import java.util.List;
import java.util.Map;

public interface ProductViewEventService {

    void save(ProductViewEvent productViewEvent);

    ProductViewEvent getProductViewEvent(Long id);

    void update(ProductViewEvent productViewEvent);

    void delete(ProductViewEvent productViewEvent);

    List<ProductViewEvent> getAllTopClicks();

    Map<Long, List<ProductViewEvent>> getAllWithPromotionId();

    Map<Long, List<Long>> getIdsFromAllWithPromotionId();

    void saveAll(List<ProductViewEvent> productViewEventList);

    void deleteAll();

}
