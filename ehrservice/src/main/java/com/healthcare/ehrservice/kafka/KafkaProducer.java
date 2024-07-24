package com.healthcare.ehrservice.kafka;

import com.healthcare.ehrservice.model.PatientRecord;
import com.healthcare.ehrservice.config.ConstantField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

	@Autowired
	private KafkaTemplate<String, PatientRecord> kafkaTemplate;
	public void produce(PatientRecord record) {
		kafkaTemplate.send(ConstantField.Kafka_Topic, record);
	}
}