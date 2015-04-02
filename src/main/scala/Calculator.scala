package main.scala

import akka.actor._
import akka.actor.ActorSystem
import akka.actor.Props
import akka.event.Logging
import main.scala.matrix.Matrix

object Calculator {
	def props = Props(new Calculator)
}

class Calculator extends Actor{
  val log = Logging(context.system, this)
	context.watch(sender)
  def receive = {
    case Matrix(rows) => {
      sender ! rows.flatten.sum
    }
		case Terminated(sender) => println("they killed my mom")
    case _ => log.error("Didn't receive a matrix")
  }
}
