package com.healthcare.ehrservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table
@Getter
@Setter
public class PatientRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;
    private String name;

    @OneToMany(mappedBy = "patientRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TreatmentHistory> treatmentHistories;

    @OneToMany(mappedBy = "patientRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LabData> labDataList;

    @OneToMany(mappedBy = "patientRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicineData> medicineDataList;

    @OneToMany(mappedBy = "patientRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diagnosis> diagnoses;
}