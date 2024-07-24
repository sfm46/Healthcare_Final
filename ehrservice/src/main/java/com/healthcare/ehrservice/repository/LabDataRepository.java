package com.healthcare.ehrservice.repository;

import com.healthcare.ehrservice.model.LabData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabDataRepository  extends JpaRepository<LabData, Long> {
}
