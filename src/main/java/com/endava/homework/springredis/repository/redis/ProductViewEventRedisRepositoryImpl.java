package com.endava.homework.springredis.repository.redis;

import com.endava.homework.springredis.model.ProductViewEvent;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class ProductViewEventRedisRepositoryImpl implements ProductViewEventRedisRepository {

    private static final String KEY = "ProductViewEventMap";
    private static final String KEY_SORTED_SET = "ProductViewEventSortedByTime";

    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, Long, ProductViewEvent> hashOperations;
    private ZSetOperations<String, Object> zSetOperations;

    private Gson gson;

    @Autowired
    public ProductViewEventRedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate, Gson gson) {
        this.redisTemplate = redisTemplate;
        this.gson = gson;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
        zSetOperations = redisTemplate.opsForZSet();
    }

    @Override
    public void save(ProductViewEvent productViewEvent) {
        hashOperations.put(KEY, productViewEvent.getId(), productViewEvent);
    }

    @Override
    public ProductViewEvent findById(Long id) {
        return hashOperations.get(KEY, id);
    }

    @Override
    public void update(ProductViewEvent productViewEvent) {
        hashOperations.put(KEY, productViewEvent.getId(), productViewEvent);
    }

    @Override
    public void delete(ProductViewEvent... productViewEvents) {
        hashOperations.delete(KEY, getProductViewEventsIdsToList(productViewEvents));
    }

    private List<Long> getProductViewEventsIdsToList(ProductViewEvent[] productViewEvents) {
        return Arrays.stream(productViewEvents).map(ProductViewEvent::getId).collect(Collectors.toList());
    }

    @Override
    public void saveAll(ProductViewEvent... productViewEvents) {
        Arrays.stream(productViewEvents).forEach(this::save);
    }

    @Override
    public void addToSet(ProductViewEvent... productViewEvents) {
        Arrays.stream(productViewEvents)
                .forEach(pve -> zSetOperations.add(KEY_SORTED_SET, gson.toJson(pve), getScore(pve)));
    }

    private long getScore(ProductViewEvent productViewEvent) {
        return productViewEvent.getDateTimeViewed().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
    }

    @Override
    public void removeLowestScoreFromSet() {
        zSetOperations.removeRange(KEY_SORTED_SET, 0L, 0L);
    }

    @Override
    public Set<Object> range(Long start, Long end) {
        return zSetOperations.range(KEY_SORTED_SET, start, end);
    }

    @Override
    public Long getSetSize() {
        return zSetOperations.size(KEY_SORTED_SET);
    }

    @Override
    public void removeAllFromSet() {
        zSetOperations.removeRange(KEY_SORTED_SET, 0, -1);
    }

    @Override
    public void removeAllFromMap() {
        hashOperations.keys(KEY)
                .forEach(currentKey -> hashOperations.delete(KEY, currentKey));
    }
}
