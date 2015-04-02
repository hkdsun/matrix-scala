package main.scala

import java.util.concurrent.TimeUnit

import akka.util.Timeout
import main.scala.matrix.Matrix
import main.scala.Calculator._
import akka.actor._
import akka.actor.ActorSystem
import akka.actor.Props
import akka.event.Logging
import akka.pattern.ask

class Dispatcher extends Actor{
	val calculator = context.actorOf(Calculator.props, name = "calculatorActor")
	context.watch(calculator)
	def receive = {
		case mat: Matrix => {
			val x = mat.pretty
			println(x)
			calculator ! mat
		}
		case x: Int => println(x)
		case Terminated(calculator) => println("they killed him :(")
		case _ => println("received garbage :(")
	}
}


object Main extends App{

  def makeMatrix(): Matrix = {
    Matrix.empty addCol (1 to 5) addRow Seq(10) addCol (20 to 25) addCol (31 to 36) addCol (15 to 20) addRow Seq(1, 1, 1, 1)
  }

	val system = ActorSystem("MatrixSystem")
	val dispatcher = system.actorOf(Props[Dispatcher], name = "dispatcherActor")
	val mat = makeMatrix
	dispatcher ! mat
	system.shutdown
}
