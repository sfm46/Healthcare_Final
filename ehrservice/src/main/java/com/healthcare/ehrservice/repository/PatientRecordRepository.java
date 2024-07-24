package com.healthcare.ehrservice.repository;

import com.healthcare.ehrservice.model.PatientRecord;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRecordRepository extends JpaRepository<PatientRecord, Long> {
    PatientRecord findByPatientId(Long patientId);
}