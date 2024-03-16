package com.g11.savingtrack.repository;

import com.g11.savingtrack.entity.Expire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpireRepository extends JpaRepository<Expire, Integer> {
}
