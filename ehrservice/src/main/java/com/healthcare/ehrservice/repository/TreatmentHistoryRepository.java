package com.healthcare.ehrservice.repository;

import com.healthcare.ehrservice.model.TreatmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentHistoryRepository extends JpaRepository<TreatmentHistory,Long> {
}
