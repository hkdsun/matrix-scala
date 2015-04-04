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
	var sum = 0
	context.watch(sender)
  def receive = {
    case Matrix(rows) => {
			rows.map(x => context.actorOf(MiniCalculator.props, name = "miniCalculatorActor") ! x)
    }
		case x: Int => {
			sum += x
			println(sum)
		}
		case Terminated(sender) => println("they killed my mom")
    case _ => log.error("Didn't receive a matrix")
  }
}

object MiniCalculator {
	def props = Props(new MiniCalculator)
}

class MiniCalculator extends Actor{
  val log = Logging(context.system, this)
	def receive = {
		case x: Vector[Int] => {
			sender ! x.sum
			println(x.sum)
		}
		case _ => log.error("received garbage")
	}
}
