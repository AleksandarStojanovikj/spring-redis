package com.endava.homework.springredis;

import com.endava.homework.springredis.exceptions.ElementAlreadyExistsException;
import com.endava.homework.springredis.model.Product;
import com.endava.homework.springredis.model.ProductItem;
import com.endava.homework.springredis.model.ProductViewEvent;
import com.endava.homework.springredis.service.ProductItemService;
import com.endava.homework.springredis.service.ProductViewEventService;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductViewEventTest {

    @Autowired
    private ProductViewEventService productViewEventService;

    @Autowired
    private ProductItemService productItemService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ProductViewEvent productViewEvent1;
    private ProductViewEvent productViewEvent2;
    private ProductViewEvent productViewEvent3;
    private ProductViewEvent productViewEvent4;

    @Before
    public void setUp() {
        productViewEvent1 = new ProductViewEvent(1L, new ProductItem(), true, true, new HashSet<>(), new HashSet<>(), 1L, LocalDateTime.now());
        productViewEvent2 = new ProductViewEvent(2L, new ProductItem(), true, true, new HashSet<>(), new HashSet<>(), 1L, LocalDateTime.now());
        productViewEvent3 = new ProductViewEvent(3L, new ProductItem(), true, true, new HashSet<>(), new HashSet<>(), 1L, LocalDateTime.now());
        productViewEvent4 = new ProductViewEvent(4L, new ProductItem(), true, false, new HashSet<>(), new HashSet<>(), 1L, LocalDateTime.now());

        productViewEventService.saveAll(Arrays.asList(productViewEvent1, productViewEvent2, productViewEvent3, productViewEvent4));
    }

    @Test
    public void shouldCreateProductViewEvent() {
        Long expectedProductViewEvent = productViewEvent1.getId();
        Long actualProductViewEvent = productViewEventService.getProductViewEvent(1L).getId();

        assertEquals(expectedProductViewEvent, actualProductViewEvent);
    }

    @Test
    public void shouldReturnProductViewEvent() {
        Long expectedProductId = productViewEvent2.getId();
        Long actualProductId = productViewEventService.getProductViewEvent(2L).getId();


        assertEquals(expectedProductId, actualProductId);
    }

    @Test
    public void shouldUpdateProductViewEvent() {
        ProductViewEvent expectedProductViewEvent = new ProductViewEvent(3L, new ProductItem(), false, false, new HashSet<>(), new HashSet<>(), 2L, LocalDateTime.now());
        productViewEventService.update(expectedProductViewEvent);

        ProductViewEvent productViewEventUpdated = productViewEventService.getProductViewEvent(3L);

        assertEquals(expectedProductViewEvent.getDateTimeViewed(), productViewEventUpdated.getDateTimeViewed());
        assertEquals(expectedProductViewEvent.isRecommendationClick(), productViewEventUpdated.isRecommendationClick());
        assertEquals(expectedProductViewEvent.isTopPickClick(), productViewEventUpdated.isTopPickClick());
    }

    @Test
    public void shouldDeleteProductViewEvent() {
        productViewEventService.delete(productViewEvent4);

        expectedException.expect(NoSuchElementException.class);
        productViewEventService.getProductViewEvent(4L);
    }

    @Test
    public void shouldReturnAllTopClickProductViewEvents() {
        List<Long> expected = Arrays.asList(productViewEvent1.getId(), productViewEvent2.getId(), productViewEvent3.getId());
        List<Long> actual = productViewEventService.getAllTopClicks().stream().map(ProductViewEvent::getId).collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnProductItemsThatAreOnPromotion() throws ElementAlreadyExistsException {
        ProductItem productItem1 = new ProductItem(1L, 123L, 1L, 10L, 14L, new Product());
        ProductItem productItem2 = new ProductItem(2L, 123L, null, 10L, 14L, new Product());
        ProductItem productItem3 = new ProductItem(3L, 123L, 2L, 10L, 14L, new Product());
        productItemService.saveAll(Arrays.asList(productItem1, productItem2, productItem3));

        productViewEvent1.setViewedProductItem(productItem1);
        productViewEventService.update(productViewEvent1);

        Map<Long, List<Long>> actualProductViewEventsOnPromotion = productViewEventService.getIdsFromAllWithPromotionId();

        Map<Long, List<Long>> expectedProductViewEventsOnPromotion = new HashMap<>();
        expectedProductViewEventsOnPromotion.put(1L, Collections.singletonList(productViewEvent1.getId()));
        expectedProductViewEventsOnPromotion.put(2L, new ArrayList<>());

        assertEquals(expectedProductViewEventsOnPromotion, actualProductViewEventsOnPromotion);
    }

    @After
    public void cleanUp() {
        productViewEventService.deleteAll();
        productItemService.deleteAll();
    }
}
