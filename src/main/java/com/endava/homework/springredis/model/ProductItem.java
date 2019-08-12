package com.endava.homework.springredis.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.Objects;

@RedisHash("ProductItem")
public class ProductItem implements Serializable {
    @Id
    private Long id;
    private long price;
    @Indexed
    private Long promotionId;
    private long inStockQuantity;
    private Long productId;
    private Product product;

    public ProductItem() {

    }

    public ProductItem(Long id, long price, Long promotionId, long inStockQuantity, Long productId, Product product) {
        this.id = id;
        this.price = price;
        this.promotionId = promotionId;
        this.inStockQuantity = inStockQuantity;
        this.productId = productId;
        this.product = product;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    public long getInStockQuantity() {
        return inStockQuantity;
    }

    public void setInStockQuantity(long inStockQuantity) {
        this.inStockQuantity = inStockQuantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductItem that = (ProductItem) o;
        return getPrice() == that.getPrice() &&
                getInStockQuantity() == that.getInStockQuantity() &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getPromotionId(), that.getPromotionId()) &&
                Objects.equals(getProductId(), that.getProductId()) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPrice(), getPromotionId(), getInStockQuantity(), getProductId(), product);
    }
}
