package com.sparfuchs.purchase;
import com.sparfuchs.store.Store;
import com.sparfuchs.storeProduct.StoreProduct;
import com.sparfuchs.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Purchase {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
    private List<StoreProduct> products;

    private Double totalPrice;

    private LocalDateTime createdAt;
}

