package com.healthcare.ehrservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class Diagnosis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diagnosisId;
    private String diagnosisName;
    private String description;

    @ManyToOne
    @JoinColumn(name = "patient_record_id")
    @JsonIgnore
    private PatientRecord patientRecord;

    // Getters and Setters
}