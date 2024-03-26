package com.g11.savingtrack.repository;

import com.g11.savingtrack.entity.Customer;
import com.g11.savingtrack.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;


@Repository
public interface OtpRepository extends JpaRepository<Otp, Integer> {
    @Query("SELECT o FROM Otp o WHERE o.customer.id = :customerId ORDER BY o.dateTimeCreate DESC")
    List<Otp> findLatestOtpByCustomerId(@Param("customerId") int customerId);

    Optional<Otp> findById(Integer integer);
}