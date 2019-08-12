package com.endava.homework.springredis.repository.redis;

import com.endava.homework.springredis.model.ProductViewEvent;

import java.util.Set;

public interface ProductViewEventRedisRepository {

    void save(ProductViewEvent productViewEvent);

    ProductViewEvent findById(Long id);

    void update(ProductViewEvent productViewEvent);

    void delete(ProductViewEvent... productViewEvents);

    void saveAll(ProductViewEvent... productViewEvents);

    void addToSet(ProductViewEvent... productViewEvents);

    void removeLowestScoreFromSet();

    Set<Object> range(Long start, Long end);

    Long getSetSize();

    void removeAllFromSet();

    void removeAllFromMap();
}
