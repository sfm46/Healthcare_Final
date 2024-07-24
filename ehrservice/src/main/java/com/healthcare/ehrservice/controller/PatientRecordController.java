package com.healthcare.ehrservice.controller;

import com.healthcare.ehrservice.interoperability.InteroperabilityService;
import com.healthcare.ehrservice.model.PatientRecord;
import com.healthcare.ehrservice.service.PatientRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/patient")
public class PatientRecordController {
    private static final Logger logger = LoggerFactory.getLogger(PatientRecordController.class);
    private final PatientRecordService patientRecordService;
    private final InteroperabilityService interoperabilityService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public PatientRecordController(PatientRecordService patientRecordService,
                                   InteroperabilityService interoperabilityService,
                                   KafkaTemplate<String, String> kafkaTemplate) {
        this.patientRecordService = patientRecordService;
        this.interoperabilityService = interoperabilityService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/test")
    public ResponseEntity<?> apiTest() {
        logger.info("API test endpoint called");
        return ResponseEntity.ok("Application is working");
    }

    @PostMapping("/createPatientRecord")
    public ResponseEntity<PatientRecord> createPatientRecord(@RequestBody PatientRecord record) {
        logger.info("Creating patient record: {}", record.toString());
        PatientRecord createdRecord =patientRecordService.createPatientRecord(record);
        CompletableFuture result=CompletableFuture.supplyAsync(() -> {
        kafkaTemplate.send("patient-records", "create", createdRecord.toString());
            return "done";
        });
        logger.info("createPatientRecord Kafka Message response ",result);
        return  new ResponseEntity<>(createdRecord, HttpStatus.OK);
    }

    @GetMapping("/getPatientRecord")
    @Cacheable(value = "patientRecords", key = "#patientId")
    public ResponseEntity<PatientRecord> getPatientRecord(@RequestParam Long patientId) {
        logger.info("Fetching patient record for ID: {}", patientId);
        return ResponseEntity.ok(patientRecordService.getPatientRecord(patientId));
    }

    @PutMapping("/updatePatientRecord")
    public ResponseEntity<PatientRecord> updatePatientRecord(@RequestBody PatientRecord record) {
        logger.info("Updating patient record: {}", record);
        PatientRecord updatedRecord = patientRecordService.updatePatientRecord(record);
        CompletableFuture result=CompletableFuture.supplyAsync(() -> {
            kafkaTemplate.send("patient-records", "update", updatedRecord.toString());
            return "done";
        });
        logger.info("updatePatientRecord Kafka Message response ",result);
        return ResponseEntity.ok(updatedRecord);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PatientRecord>> getAllPatientRecords() {
        logger.info("Fetching all patient records");
        return ResponseEntity.ok(patientRecordService.getAllPatientRecords());
    }

    @DeleteMapping("/deletePatientRecord/{id}")
    public ResponseEntity<String> deletePatientRecord(@PathVariable Long id) {
        logger.info("Deleting patient record with ID: {}", id);
        patientRecordService.deletePatientRecord(id);
        CompletableFuture result=  CompletableFuture.runAsync(() -> {
            kafkaTemplate.send("patient-records", "delete", id.toString());
        });
        logger.info("deletePatientRecord Kafka Message response ",result);
        return ResponseEntity.ok("patient deleted successfully "+id);
    }

    @GetMapping("/exportHL7")
    public ResponseEntity<String> exportHL7(@RequestParam Long patientId) {
        logger.info("Exporting patient record to HL7 format for ID: {}", patientId);
        PatientRecord patientRecord = patientRecordService.getPatientRecord(patientId);
        String hl7Format = interoperabilityService.convertToHL7Format(patientRecord);
        CompletableFuture result=  CompletableFuture.supplyAsync(() -> {
            kafkaTemplate.send("patient-records", "export", hl7Format);
            return hl7Format;
        });
        logger.info("exportHL7 Kafka Message response ",result);
        return ResponseEntity.ok(hl7Format);
    }
}