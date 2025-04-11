package com.sparfuchs.storeProduct;

import com.sparfuchs.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreProductRepository extends JpaRepository<StoreProduct, Long> {
    List<StoreProduct> findTop5ByProductAndStoreId(Product product, Long storeId);
    Optional<StoreProduct> findByProductAndStoreId(Product product, Long storeId);
}

