package com.healthcare.ehrservice.repository;

import com.healthcare.ehrservice.model.MedicineData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineDataRepository extends JpaRepository<MedicineData,Long> {
}
