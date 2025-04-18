package com.sparfuchs.storeProduct;

import com.sparfuchs.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StoreProductRepository extends JpaRepository<StoreProduct, Long> {
    Optional<StoreProduct> findByProductAndStoreId(Product product, Long storeId);
}

