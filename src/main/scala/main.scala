package main.scala

import java.util.concurrent.TimeUnit

import akka.util.Timeout
import main.scala.matrix.Matrix
import main.scala.Calculator
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.event.Logging
import akka.pattern.ask
import org.scalatest.time.Seconds

class Dispatcher extends Actor {
  val mat = Main.makeMatrix()
  val actor = system.actorOf(Props(new Calculator))
  actor ? mat
  def receive = {
    case x:Int => println(x)
    case _ => println("received garbage")
  }
}

object Main{

  def makeMatrix(): Unit = {
    Matrix.empty addCol (1 to 5) addRow Seq(10) addCol (20 to 25) addCol (31 to 36) addCol (15 to 20) addRow Seq(1, 1, 1, 1)
  }

  def main(args:Array[String]) = {
    val system = ActorSystem("System")
    val actor = system.actorOf(Props(new Dispatcher))
    implicit val timeout = Timeout(25,TimeUnit.SECONDS)
  }

}
