package com.g11.savingtrack.repository;

import com.g11.savingtrack.entity.SavingProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingProductRepository extends JpaRepository<SavingProduct, Integer> {
}
