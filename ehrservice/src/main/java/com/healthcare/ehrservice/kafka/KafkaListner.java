package com.healthcare.ehrservice.kafka;

import com.healthcare.ehrservice.config.ConstantField;
import com.healthcare.ehrservice.model.PatientRecord;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class KafkaListner {

	private Logger logger= LoggerFactory.getLogger(KafkaListner.class);

	@KafkaListener(topics = ConstantField.Kafka_Topic, groupId = ConstantField.Kafka_Group)
	public void consume(PatientRecord record) {
		logger.info("Patient Record is stored in Kafka.");
		logger.info("Record for::-> PatientId " + record.getPatientId());
	}
}