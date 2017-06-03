package com.broilogabriel

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Producer
import akka.kafka.{ProducerMessage, ProducerSettings}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by broilogabriel on 5/27/2017.
  */
object Main extends App {

  implicit val system = ActorSystem.create("janus")
  implicit val materializer = ActorMaterializer()

  val producerSettings = ProducerSettings(system, new ByteArraySerializer, new StringSerializer)
    .withBootstrapServers("localhost:9092")
  //  val kafkaProducer = producerSettings.createKafkaProducer()


  println(s"Produce the ${Hello.world}")

  val done = Source(1 to 100)
    .map {
      n =>
        // val partition = math.abs(n) % 2
        val partition = 0
        ProducerMessage.Message(new ProducerRecord[Array[Byte], String](
          "janus", partition, null, n.toString
        ), n)
    }
    .via(Producer.flow(producerSettings))
    .map {
      result =>
        val record = result.message.record
        println(s"${record.topic}/${record.partition} ${result.offset}: ${record.value}" +
          s"(${result.message.passThrough})")
        result
    }
    .runWith(Sink.ignore)


  done.onComplete(_ => system.terminate())
}

