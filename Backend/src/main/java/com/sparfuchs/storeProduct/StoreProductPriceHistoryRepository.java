package com.sparfuchs.storeProduct;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreProductPriceHistoryRepository extends JpaRepository<StoreProductPriceHistory, Long> {
    List<StoreProductPriceHistory> findByStoreProduct(StoreProduct storeProduct);
}
