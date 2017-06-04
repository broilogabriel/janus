package com.broilogabriel

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import org.joda.time.Instant

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationDouble

object Main extends App {

  implicit val system = ActorSystem.create("janus")
  implicit val materializer = ActorMaterializer()

  val (ref, pub) = Source.actorRef[String](0, OverflowStrategy.fail)
    .via(Flow.fromFunction({
      m =>
        val n = m.toLowerCase()
        println(n)
        n
    }))
    .toMat(Sink.ignore)(Keep.both)
    .run()

  system.scheduler.schedule(1.second, 10.seconds) {
    ref ! s"RUN, FOREST RUN ${Instant.now()}"
  }
  println(s"App the ${Hello.world}")

}


