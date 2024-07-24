package com.healthcare.ehrservice.service;

import com.healthcare.ehrservice.model.*;
import com.healthcare.ehrservice.repository.PatientRecordRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class PatientRecordServiceTest {

    @Autowired
    private PatientRecordService patientRecordService;

    @MockBean
    private PatientRecordRepository patientRecordRepository;

    private PatientRecord patientRecord;

    @BeforeEach
    void setUp() {
        // Initialize test data
        patientRecord = new PatientRecord();
        patientRecord.setPatientId(1L);
        patientRecord.setLabDataList(Arrays.asList(new LabData(), new LabData()));
        patientRecord.setMedicineDataList(Arrays.asList(new MedicineData(), new MedicineData()));
        patientRecord.setDiagnoses(Arrays.asList(new Diagnosis(), new Diagnosis()));
        patientRecord.setTreatmentHistories(Arrays.asList(new TreatmentHistory(), new TreatmentHistory()));
    }

    @Test
    void testGetPatientRecord() {
        when(patientRecordRepository.findByPatientId(1L)).thenReturn(patientRecord);

        PatientRecord result = patientRecordService.getPatientRecord(1L);

        assertNotNull(result);
        assertEquals(1L, result.getPatientId());
        verify(patientRecordRepository, times(1)).findByPatientId(1L);
    }
    @Test
    void testGetPatientRecord_NotFound() {
        when(patientRecordRepository.findByPatientId(anyLong())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            patientRecordService.getPatientRecord(1L);
        });

        verify(patientRecordRepository, times(1)).findByPatientId(1L);
    }

    @Test
    void testUpdatePatientRecord() {
        when(patientRecordRepository.save(any(PatientRecord.class))).thenReturn(patientRecord);

        PatientRecord result = patientRecordService.updatePatientRecord(patientRecord);

        assertNotNull(result);
        assertEquals(1L, result.getPatientId());
        verify(patientRecordRepository, times(1)).save(patientRecord);
    }

    @Test
    void testGetAllPatientRecords() {
        when(patientRecordRepository.findAll()).thenReturn(Arrays.asList(patientRecord));

        List<PatientRecord> result = patientRecordService.getAllPatientRecords();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getPatientId());
        verify(patientRecordRepository, times(1)).findAll();
    }

    @Test
    void testCreatePatientRecord() {
        when(patientRecordRepository.save(any(PatientRecord.class))).thenReturn(patientRecord);

        PatientRecord result = patientRecordService.createPatientRecord(patientRecord);

        assertNotNull(result);
        assertEquals(1L, result.getPatientId());
        verify(patientRecordRepository, times(1)).save(patientRecord);
    }

    @Test
    void testDeletePatientRecord() {
        doNothing().when(patientRecordRepository).deleteById(1L);

        patientRecordService.deletePatientRecord(1L);

        verify(patientRecordRepository, times(1)).deleteById(1L);
    }
}
