package com.healthcare.ehrservice.repository;

import com.healthcare.ehrservice.model.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis,Long> {
}
