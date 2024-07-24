package com.healthcare.ehrservice.service;

import com.healthcare.ehrservice.model.*;
import com.healthcare.ehrservice.repository.*;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PatientRecordService {

    @Autowired
    private PatientRecordRepository patientRecordRepository;
    @Autowired
    private LabDataRepository labDataRepository;
    @Autowired
    private TreatmentHistoryRepository treatmentHistoryRepository;
    @Autowired
    private MedicineDataRepository medicineDataRepository;
    @Autowired
    private DiagnosisRepository diagnosisRepository;


    public PatientRecord getPatientRecord(Long patientId) {
        return  patientRecordRepository.findByPatientId(patientId);
    }

    public PatientRecord updatePatientRecord(PatientRecord updatedRecord) {
        // Retrieve the existing patient record
        PatientRecord existingRecord = patientRecordRepository.findById(updatedRecord.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        // Update fields
        existingRecord.setName(updatedRecord.getName());

        // Update treatment histories
        if (updatedRecord.getTreatmentHistories() != null) {
            for (TreatmentHistory updatedHistory : updatedRecord.getTreatmentHistories()) {
                TreatmentHistory existingHistory = treatmentHistoryRepository.findById(updatedHistory.getTreatmentHistoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("Treatment History not found"));
                existingHistory.setTreatmentDescription(updatedHistory.getTreatmentDescription());
            }
        }

        // Update lab data
        if (updatedRecord.getLabDataList() != null) {
            for (LabData updatedLabData : updatedRecord.getLabDataList()) {
                LabData existingLabData = labDataRepository.findById(updatedLabData.getLabDataId())
                        .orElseThrow(() -> new ResourceNotFoundException("Lab Data not found"));
                existingLabData.setTestName(updatedLabData.getTestName());
                existingLabData.setResult(updatedLabData.getResult());
            }
        }

        // Update medicine data
        if (updatedRecord.getMedicineDataList() != null) {
            for (MedicineData updatedMedicineData : updatedRecord.getMedicineDataList()) {
                MedicineData existingMedicineData = medicineDataRepository.findById(updatedMedicineData.getMedicineId())
                        .orElseThrow(() -> new ResourceNotFoundException("Medicine Data not found"));
                existingMedicineData.setMedicineName(updatedMedicineData.getMedicineName());
                existingMedicineData.setDosage(updatedMedicineData.getDosage());
            }
        }

        // Update diagnoses
        if (updatedRecord.getDiagnoses() != null) {
            for (Diagnosis updatedDiagnosis : updatedRecord.getDiagnoses()) {
                Diagnosis existingDiagnosis = diagnosisRepository.findById(updatedDiagnosis.getDiagnosisId())
                        .orElseThrow(() -> new ResourceNotFoundException("Diagnosis not found"));
                existingDiagnosis.setDiagnosisName(updatedDiagnosis.getDiagnosisName());
                existingDiagnosis.setDescription(updatedDiagnosis.getDescription());
            }
        }

        // Save and return updated record
        return patientRecordRepository.save(existingRecord);
    }

    public List<PatientRecord> getAllPatientRecords() {
        return patientRecordRepository.findAll();
    }
    @Transactional
    public PatientRecord createPatientRecord(PatientRecord record) {
        // Save the patient record first
        PatientRecord savedRecord = patientRecordRepository.save(record);

        // Set the patientId in each related entity
        if (savedRecord.getLabDataList() != null) {
            for (LabData labData : savedRecord.getLabDataList()) {
                labData.setPatientRecord(savedRecord);
            }
        }
        if (savedRecord.getMedicineDataList() != null) {
            for (MedicineData medicineData : savedRecord.getMedicineDataList()) {
                medicineData.setPatientRecord(savedRecord);
            }
        }
        if (savedRecord.getDiagnoses() != null) {
            for (Diagnosis diagnosis : savedRecord.getDiagnoses()) {
                diagnosis.setPatientRecord(savedRecord);
            }
        }
        if (savedRecord.getTreatmentHistories() != null) {
            for (TreatmentHistory treatmentHistory : savedRecord.getTreatmentHistories()) {
                treatmentHistory.setPatientRecord(savedRecord);
            }
        }

        // Save the updated patient record with the related entities
        return patientRecordRepository.save(savedRecord);
    }

    public void deletePatientRecord(Long id) {
        patientRecordRepository.deleteById(id);
    }

}