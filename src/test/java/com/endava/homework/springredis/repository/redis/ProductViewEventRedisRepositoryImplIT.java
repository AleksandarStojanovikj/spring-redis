package com.endava.homework.springredis.repository.redis;

import com.endava.homework.springredis.model.ProductItem;
import com.endava.homework.springredis.model.ProductViewEvent;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductViewEventRedisRepositoryImplIT {

    @Autowired
    private ProductViewEventRedisRepository productViewEventRedisRepository;

    @Autowired
    private Gson gson;

    private ProductViewEvent productViewEvent1;
    private ProductViewEvent productViewEvent2;
    private ProductViewEvent productViewEvent3;
    private ProductViewEvent productViewEvent4;

    @Before
    public void setUp() {
        productViewEvent1 = new ProductViewEvent(1L, new ProductItem(), false, true, new HashSet<>(), new HashSet<>(), 20L, LocalDateTime.now().minusMinutes(1L));
        productViewEvent2 = new ProductViewEvent(2L, new ProductItem(), false, true, new HashSet<>(), new HashSet<>(), 20L, LocalDateTime.now().minusMinutes(3L));
        productViewEvent3 = new ProductViewEvent(3L, new ProductItem(), false, true, new HashSet<>(), new HashSet<>(), 20L, LocalDateTime.now().minusMinutes(2L));
        productViewEvent4 = new ProductViewEvent(4L, new ProductItem(), false, true, new HashSet<>(), new HashSet<>(), 20L, LocalDateTime.now().minusMinutes(4L));

        productViewEventRedisRepository.saveAll(productViewEvent1, productViewEvent2, productViewEvent3, productViewEvent4);
        productViewEventRedisRepository.addToSet(productViewEvent1, productViewEvent2, productViewEvent3, productViewEvent4);
    }


    @Test
    public void shouldSaveProductViewEvent() {
        ProductViewEvent productViewEvent5 = new ProductViewEvent(5L, new ProductItem(), false, true, new HashSet<>(), new HashSet<>(), 20L, LocalDateTime.now().minusMinutes(4L));
        productViewEventRedisRepository.save(productViewEvent5);
    }

    @Test
    public void shouldAddProductViewEventToSortedSet() {
        ProductViewEvent productViewEvent5 = new ProductViewEvent(5L, new ProductItem(), false, true, new HashSet<>(), new HashSet<>(), 20L, LocalDateTime.now().minusMinutes(5L));
        productViewEventRedisRepository.addToSet(productViewEvent5);

        Set<Object> productViewEvents = productViewEventRedisRepository.range(0L, -1L);
        Set<Object> expectedProductViewEvents = Stream.of(toJson(productViewEvent5), toJson(productViewEvent4),
                toJson(productViewEvent2), toJson(productViewEvent3), toJson(productViewEvent1))
                .collect(Collectors.toSet());

        assertEquals(expectedProductViewEvents, productViewEvents);
    }

    private String toJson(Object object) {
        return gson.toJson(object);
    }

    @Test
    public void shouldRetrieveValuesProductViewEventFromSortedSet() {
        Set<Object> productViewEvents = productViewEventRedisRepository.range(0L, 2L);
        Set<Object> expectedProductViewEvents = Stream.of(
                toJson(productViewEvent4),
                toJson(productViewEvent2),
                toJson(productViewEvent3))
                .collect(Collectors.toSet());

        assertEquals(expectedProductViewEvents, productViewEvents);
    }

    @Test
    public void shouldRemoveAndRetrieveTheMemberWithTheLowestScore() {
        productViewEventRedisRepository.removeLowestScoreFromSet();

        Set<Object> productViewEventWithLowestScore = productViewEventRedisRepository.range(0L, 0L);
        Set<Object> expectedProductViewEventWithLowestScore = Stream.of(toJson(productViewEvent2)).collect(Collectors.toSet());

        assertEquals(expectedProductViewEventWithLowestScore, productViewEventWithLowestScore);
    }

    @Test
    public void shouldCountTheMembersInTheSet() {
        Long expectedSetSize = 4L;
        Long actualSetSize = productViewEventRedisRepository.getSetSize();

        assertEquals(expectedSetSize, actualSetSize);
    }

    @After
    public void cleanUpDatabase() {
        productViewEventRedisRepository.removeAllFromMap();
        productViewEventRedisRepository.removeAllFromSet();
    }
}