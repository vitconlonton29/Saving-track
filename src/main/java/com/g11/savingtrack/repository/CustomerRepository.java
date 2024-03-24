package com.g11.savingtrack.repository;

import com.g11.savingtrack.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
  Optional<Customer> findByIdentityCardNumber(String identity);


}
