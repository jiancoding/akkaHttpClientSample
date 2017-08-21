package producer

import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import com.typesafe.scalalogging.LazyLogging
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}

object dataProducer extends LazyLogging{

  implicit class InternalProducer(kafkaProducer: KafkaProducer[Nothing, GenericRecord]) {
    implicit val system = ActorSystem("StockDataProducerFlow")

    val keySerializer = new ByteArraySerializer
    val valueSerializer = new StringSerializer
    val topic = "topic1"
    val producerSettings = ProducerSettings(system, keySerializer, valueSerializer)

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
