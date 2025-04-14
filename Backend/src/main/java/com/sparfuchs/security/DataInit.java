package com.sparfuchs.security;

import com.sparfuchs.store.Store;
import com.sparfuchs.store.StoreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements CommandLineRunner {

    StoreRepository storeRepository;

    public DataInit(StoreRepository storerepo) {
        this.storeRepository = storerepo;
    }

    @Override
    public void run(String... args) throws Exception {
        storeRepository.save(new Store("Spar"));
        storeRepository.save(new Store("Billa"));
    }
}

