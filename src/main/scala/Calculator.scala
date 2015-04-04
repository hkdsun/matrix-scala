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
	var count = 0
	val mini = context.actorOf(MiniCalculator.props, name = "miniCalculatorActor")
	
	def receive = {
    case Matrix(rows) => {
			count = rows.length
			rows.map(x => mini ! x)
    }
		case x: Int => {
			sum += x
			count -= 1
			println("calc sum = " + sum)
			if(count<1) {
				context.parent ! sum
				context.stop(self)
			}
		}
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
			println("mini calc sum = " + x.sum)
		}
		case _ => log.error("received garbage")
	}
}
