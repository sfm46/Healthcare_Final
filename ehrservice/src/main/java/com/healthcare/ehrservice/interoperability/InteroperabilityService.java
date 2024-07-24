package com.healthcare.ehrservice.interoperability;

import com.healthcare.ehrservice.model.PatientRecord;
import org.springframework.stereotype.Service;

@Service
public class InteroperabilityService {


    public String convertToHL7Format(PatientRecord patientData) {
       return Hl7MessageBuilder.buildHl7Message(patientData);
    }


}