package com.sparfuchs.purchaseProduct;

import com.sparfuchs.storeProduct.StoreProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseProductRepository extends JpaRepository<PurchaseProduct, Long> {
    // nötig?
}
