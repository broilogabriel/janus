package com.broilogabriel

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Producer
import akka.kafka.{ProducerMessage, ProducerSettings}
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}
import org.joda.time.Instant

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationDouble

object Main extends App {

  implicit val system = ActorSystem.create("janus")
  implicit val materializer = ActorMaterializer()

  val producerSettings = ProducerSettings(system, new ByteArraySerializer, new StringSerializer)
    .withBootstrapServers("localhost:9092")
  //  val kafkaProducer = producerSettings.createKafkaProducer()

  println(s"Produce the ${Hello.world}")

  val ref = Source.actorRef[String](0, OverflowStrategy.fail)
    .map {
      message =>
        // val partition = math.abs(n) % 2
        val partition = 0
        ProducerMessage.Message(
          new ProducerRecord[Array[Byte], String]("janus", partition, null, message),
          message
        )
    }
    .via(Producer.flow(producerSettings))
    .map {
      result =>
        val record = result.message.record
        println(s"${record.topic}/${record.partition} ${result.offset}: ${record.value}" +
          s"(${result.message.passThrough})")
        result
    }
    .toMat(Sink.ignore)(Keep.left)
    .run()

  system.scheduler.schedule(1.second, 10.seconds) {
    ref ! s"RUN, FOREST RUN ${Instant.now()}"
  }
}


