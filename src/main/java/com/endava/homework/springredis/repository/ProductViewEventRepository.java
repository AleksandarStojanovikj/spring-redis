package com.endava.homework.springredis.repository;

import com.endava.homework.springredis.model.ProductViewEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductViewEventRepository extends CrudRepository<ProductViewEvent, Long> {
    List<ProductViewEvent> findAllByIsTopPickClick(boolean topClick);

    List<ProductViewEvent> findAllByViewedProductItemPromotionId(Long promotionId);
}
