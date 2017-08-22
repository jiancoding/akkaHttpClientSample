package producer

import com.typesafe.scalalogging.LazyLogging
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}

object dataProducer extends LazyLogging{

  //todo 1. build ProducerRecord with specific topic name
  implicit class InternalProducer(kafkaProducer: KafkaProducer[Nothing, GenericRecord]) {

    def send(recrods: Seq[ProducerRecord[Nothing, GenericRecord]]) = {
      recrods.foreach(sendOneReocrd)
    }

    def sendOneReocrd(record: ProducerRecord[Nothing, GenericRecord]) = {
      val callback = new Callback() {
        def onCompletion(metadata: RecordMetadata, e: Exception) {
          if (e != null) {
            logger.error("Failed to produce record: " + metadata + " with exception " + e)
            e.printStackTrace()
          } else {
            logger.info("The offset of the record we just sent is: " + metadata.offset())
          }
        }
      }
      kafkaProducer.send(record, callback)
    }
  }
}
