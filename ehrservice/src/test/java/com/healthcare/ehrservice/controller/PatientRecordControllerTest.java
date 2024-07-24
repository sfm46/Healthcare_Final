package com.healthcare.ehrservice.controller;

import com.healthcare.ehrservice.model.PatientRecord;
import com.healthcare.ehrservice.service.PatientRecordService;
import com.healthcare.ehrservice.interoperability.InteroperabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PatientRecordControllerTest {

    @InjectMocks
    private PatientRecordController patientRecordController;

    @Mock
    private PatientRecordService patientRecordService;

    @Mock
    private InteroperabilityService interoperabilityService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testApiTest() {
        ResponseEntity<?> response = patientRecordController.apiTest();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Application is working", response.getBody());
    }

    @Test
    void testCreatePatientRecord() {
        PatientRecord record = new PatientRecord();
        when(patientRecordService.createPatientRecord(any(PatientRecord.class))).thenReturn(record);

        ResponseEntity<PatientRecord> response = patientRecordController.createPatientRecord(record);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(record, response.getBody());
        verify(kafkaTemplate, times(1)).send(anyString(), anyString(), anyString());
    }

    @Test
    void testGetPatientRecord() {
        PatientRecord record = new PatientRecord();
        when(patientRecordService.getPatientRecord(anyLong())).thenReturn(record);

        ResponseEntity<PatientRecord> response = patientRecordController.getPatientRecord(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(record, response.getBody());
    }

    @Test
    void testUpdatePatientRecord() {
        PatientRecord record = new PatientRecord();
        when(patientRecordService.updatePatientRecord(any(PatientRecord.class))).thenReturn(record);

        ResponseEntity<PatientRecord> response = patientRecordController.updatePatientRecord(record);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(record, response.getBody());
        verify(kafkaTemplate, times(1)).send(anyString(), anyString(), anyString());
    }

    @Test
    void testGetAllPatientRecords() {
        List<PatientRecord> records = Collections.singletonList(new PatientRecord());
        when(patientRecordService.getAllPatientRecords()).thenReturn(records);

        ResponseEntity<List<PatientRecord>> response = patientRecordController.getAllPatientRecords();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(records, response.getBody());
    }

    @Test
    void testDeletePatientRecord() {
        ResponseEntity<String> response = patientRecordController.deletePatientRecord(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("patient deleted successfully 1", response.getBody());
        verify(kafkaTemplate, times(1)).send(anyString(), anyString(), anyString());
    }

    @Test
    void testExportHL7() {
        PatientRecord record = new PatientRecord();
        when(patientRecordService.getPatientRecord(anyLong())).thenReturn(record);
        when(interoperabilityService.convertToHL7Format(any(PatientRecord.class))).thenReturn("HL7");

        ResponseEntity<String> response = patientRecordController.exportHL7(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("HL7", response.getBody());
        verify(kafkaTemplate, times(1)).send(anyString(), anyString(), anyString());
    }
}
