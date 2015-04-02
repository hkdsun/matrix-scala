package main.scala

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.event.Logging
import main.scala.matrix.Matrix

class Calculator extends Actor{
  val log = Logging(context.system, this)
  def receive = {
    case Matrix(rows) => {
      sender ! rows.flatten.sum
    }
    case _ => log.error("Didn't receive a matrix")
  }
}
