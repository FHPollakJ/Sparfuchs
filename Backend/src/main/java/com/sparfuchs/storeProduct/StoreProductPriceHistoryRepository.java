package com.sparfuchs.storeProduct;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StoreProductPriceHistoryRepository extends JpaRepository<StoreProductPriceHistory, Long> {
    Optional<List<StoreProductPriceHistory>> findByStoreProduct(StoreProduct storeProduct);
}
