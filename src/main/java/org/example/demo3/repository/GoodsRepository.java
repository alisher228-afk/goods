package org.example.demo3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.demo3.entity.GoodsForDb;

public interface GoodsRepository extends JpaRepository<GoodsForDb, Long> {
    boolean existsByNameIgnoreCase(String name);

}
