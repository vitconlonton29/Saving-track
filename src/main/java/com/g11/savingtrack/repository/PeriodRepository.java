package com.g11.savingtrack.repository;

import com.g11.savingtrack.entity.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Integer> {
  Period findByMonth(int month);
}
