package com.endava.homework.springredis.service.impl;

import com.endava.homework.springredis.exceptions.ElementAlreadyExistsException;
import com.endava.homework.springredis.model.ProductViewEvent;
import com.endava.homework.springredis.repository.ProductViewEventRepository;
import com.endava.homework.springredis.service.ProductItemService;
import com.endava.homework.springredis.service.ProductViewEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductViewEventServiceImpl implements ProductViewEventService {

    private final ProductViewEventRepository productViewEventRepository;

    private ProductItemService productItemService;

    @Autowired
    public ProductViewEventServiceImpl(ProductViewEventRepository productViewEventRepository, ProductItemService productItemService) {
        this.productViewEventRepository = productViewEventRepository;
        this.productItemService = productItemService;
    }

    @Override
    public void save(ProductViewEvent productViewEvent) {
        if (isPresent(productViewEvent)) {
            throw new ElementAlreadyExistsException();
        }

        productViewEventRepository.save(productViewEvent);
    }

    @Override
    public void saveAll(List<ProductViewEvent> productViewEventList) {
        productViewEventRepository.saveAll(productViewEventList);
    }

    private boolean isPresent(ProductViewEvent productViewEvent) {
        return productViewEventRepository.findById(productViewEvent.getId()).isPresent();
    }

    @Override
    public ProductViewEvent getProductViewEvent(Long id) {
        return productViewEventRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void update(ProductViewEvent productViewEvent) {
        ProductViewEvent product = productViewEventRepository
                .findById(productViewEvent.getId())
                .orElseThrow(NoSuchElementException::new);

        product.setViewedProductItem(productViewEvent.getViewedProductItem());
        product.setRecommendationClick(productViewEvent.isRecommendationClick());
        product.setTopPickClick(productViewEvent.isTopPickClick());
        product.setPersonalRecommendedProductItems(productViewEvent.getPersonalRecommendedProductItems());
        product.setTopPicksProductItems(productViewEvent.getTopPicksProductItems());
        product.setUserId(productViewEvent.getUserId());
        product.setDateTimeViewed(productViewEvent.getDateTimeViewed());

        productViewEventRepository.save(product);
    }

    @Override
    public void delete(ProductViewEvent productViewEvent) {
        productViewEventRepository.delete(productViewEvent);
    }

    @Override
    public List<ProductViewEvent> getAllTopClicks() {
        return productViewEventRepository.findAllByIsTopPickClick(true);
    }

    @Override
    public Map<Long, List<ProductViewEvent>> getAllWithPromotionId() {
        Set<Long> promotionIds = productItemService.getAllPromotionIds();

        Map<Long, List<ProductViewEvent>> productViewEventsOnPromotion = new HashMap<>();

        promotionIds.forEach(promotionId ->
                productViewEventsOnPromotion.put(promotionId, productViewEventRepository.findAllByViewedProductItemPromotionId(promotionId))
        );

        return productViewEventsOnPromotion;
    }

    @Override
    public Map<Long, List<Long>> getIdsFromAllWithPromotionId() {
        Set<Long> promotionIds = productItemService.getAllPromotionIds();

        Map<Long, List<Long>> productViewEventsOnPromotion = new HashMap<>();

        promotionIds.forEach(promotionId ->
                productViewEventsOnPromotion.put(promotionId,
                        productViewEventRepository.findAllByViewedProductItemPromotionId(promotionId).stream()
                                .map(ProductViewEvent::getId).collect(Collectors.toList()))
        );

        return productViewEventsOnPromotion;
    }

    @Override
    public void deleteAll() {
        productViewEventRepository.deleteAll();
    }
}
