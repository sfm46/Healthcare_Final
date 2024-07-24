package com.healthcare.ehrservice.interoperability;

import com.healthcare.ehrservice.model.PatientRecord;
import com.healthcare.ehrservice.model.TreatmentHistory;
import com.healthcare.ehrservice.model.LabData;
import com.healthcare.ehrservice.model.MedicineData;
import com.healthcare.ehrservice.model.Diagnosis;

import java.text.SimpleDateFormat;
import java.util.List;

public class Hl7MessageBuilder {

    private static final String HL7_SEPARATOR = "|";
    private static final String HL7_FIELD_SEPARATOR = "^";
    private static final String DATE_FORMAT = "yyyyMMddHHmmss";

    public static String buildHl7Message(PatientRecord patientRecord) {
        StringBuilder hl7Message = new StringBuilder();

        // MSH Segment
        hl7Message.append(buildMshSegment());

        // PID Segment
        hl7Message.append(buildPidSegment(patientRecord));

        // Treatment Histories
        for (TreatmentHistory history : patientRecord.getTreatmentHistories()) {
            hl7Message.append(buildTreatmentHistorySegment(history));
        }

        // Lab Data
        for (LabData labData : patientRecord.getLabDataList()) {
            hl7Message.append(buildLabDataSegment(labData));
        }

        // Medicine Data
        for (MedicineData medicine : patientRecord.getMedicineDataList()) {
            hl7Message.append(buildMedicineDataSegment(medicine));
        }

        // Diagnoses
        for (Diagnosis diagnosis : patientRecord.getDiagnoses()) {
            hl7Message.append(buildDiagnosisSegment(diagnosis));
        }

        return hl7Message.toString();
    }

    private static String buildMshSegment() {
        return "MSH" + HL7_SEPARATOR + "^~\\&" + HL7_SEPARATOR + "SendingApp" + HL7_SEPARATOR + "SendingFacility" +
                HL7_SEPARATOR + "ReceivingApp" + HL7_SEPARATOR + "ReceivingFacility" + HL7_SEPARATOR + getCurrentTimestamp() +
                HL7_SEPARATOR + HL7_SEPARATOR + "ADT^A01" + HL7_SEPARATOR + "MSG00001" + HL7_SEPARATOR + "P" + HL7_SEPARATOR +
                "2.3" + "\n";
    }

    private static String buildPidSegment(PatientRecord patientRecord) {
        return "PID" + HL7_SEPARATOR + HL7_SEPARATOR + patientRecord.getPatientId() + HL7_SEPARATOR + HL7_SEPARATOR +
                patientRecord.getName() + HL7_SEPARATOR + HL7_SEPARATOR + "\n";
    }

    private static String buildTreatmentHistorySegment(TreatmentHistory history) {
        return "OBX" + HL7_SEPARATOR + HL7_SEPARATOR + "TX" + HL7_SEPARATOR + "TreatmentHistory" + HL7_SEPARATOR + HL7_SEPARATOR +
                history.getTreatmentDescription() + HL7_SEPARATOR + HL7_SEPARATOR + "\n";
    }

    private static String buildLabDataSegment(LabData labData) {
        return "OBX" + HL7_SEPARATOR + HL7_SEPARATOR + "TX" + HL7_SEPARATOR + "LabData" + HL7_SEPARATOR + labData.getTestName() +
                HL7_FIELD_SEPARATOR + labData.getResult() + HL7_SEPARATOR + HL7_SEPARATOR + "\n";
    }

    private static String buildMedicineDataSegment(MedicineData medicine) {
        return "OBX" + HL7_SEPARATOR + HL7_SEPARATOR + "TX" + HL7_SEPARATOR + "MedicineData" + HL7_SEPARATOR + medicine.getMedicineName() +
                HL7_FIELD_SEPARATOR + medicine.getDosage() + HL7_SEPARATOR + HL7_SEPARATOR + "\n";
    }

    private static String buildDiagnosisSegment(Diagnosis diagnosis) {
        return "OBX" + HL7_SEPARATOR + HL7_SEPARATOR + "TX" + HL7_SEPARATOR + "Diagnosis" + HL7_SEPARATOR + diagnosis.getDiagnosisName() +
                HL7_FIELD_SEPARATOR + diagnosis.getDescription() + HL7_SEPARATOR + HL7_SEPARATOR + "\n";
    }

    private static String getCurrentTimestamp() {
        return new SimpleDateFormat(DATE_FORMAT).format(new java.util.Date());
    }
}