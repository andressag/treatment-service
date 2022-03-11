package com.connecting.repository;

import com.connecting.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    Optional<Treatment> findByName(String name);
}
