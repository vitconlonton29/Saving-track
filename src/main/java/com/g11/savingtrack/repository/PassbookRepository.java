package com.g11.savingtrack.repository;

import com.g11.savingtrack.entity.Passbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassbookRepository extends JpaRepository<Passbook, Integer> {
}
