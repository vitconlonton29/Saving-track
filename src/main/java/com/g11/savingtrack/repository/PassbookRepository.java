package com.g11.savingtrack.repository;

import com.g11.savingtrack.entity.Passbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassbookRepository extends JpaRepository<Passbook, Integer> {
    @Query("SELECT p from Passbook p where p.customer.identityCardNumber = :cccd")
    List<Passbook> getPassbooksByCCCD(@Param("cccd") String cccd);

    @Query("SELECT p from Passbook p where p.customer.id = :customerId")
    List<Passbook> findAllByCustomerId(@Param("customerId") int customerId);


}
