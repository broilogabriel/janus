package com.broilogabriel

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}


object Main extends App {

  val f: Future[Int] = Future.successful {
    Thread.sleep(5000)
    1
  }

  val result = Await.ready(f, 5 seconds)

  println(result.flatten(_))

}