package com.endava.homework.springredis.repository;

import com.endava.homework.springredis.model.ProductItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductItemRepository extends CrudRepository<ProductItem, Long> {
    List<ProductItem> findAll();
}
