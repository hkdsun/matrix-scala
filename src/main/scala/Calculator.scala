package main.scala

import akka.actor._
import akka.actor.ActorSystem
import akka.actor.Props
import akka.event.Logging

object Calculator {
	def props = Props(new Calculator)
}

class Calculator extends Actor{
  
	val log = Logging(context.system, this)
	var caller = self
	val 
	
	def receive = {
    case group: Seq[Matrix] => {
			for(i <- 0 until group.size){
				caller = sender
				val mini = context.actorOf(MiniCalculator.props(i), name = ("miniCalculatorActor"+i))
				mini ! group
			}
    }
		case result: Matrix => {
			caller ! result
			context.stop(self)
		}
    case _ => log.error("Didn't receive a matrix")
  }
}

object MiniCalculator {
	def props(index: Int) = Props(classOf[MiniCalculator],index)
}

class MiniCalculator(index: Int) extends Actor{
  val log = Logging(context.system, this)
	val aggregator = context.actorOf(Aggregator.props
	def receive = {
		case x: Seq[Matrix] => {
			val pair = Tuple2(index,x.reduce { (left, right) => left * right })
		}
		case _ => log.error("MiniCalculator received garbage")
	}
}

object Aggregator {
	def props = Props(new Aggregator)
}

class Aggregator extends Actor{
  val log = Logging(context.system, this)
	var totalCount = 0
	var seqOfMatrices: Seq[Tuple2[Int,Matrix]] = Seq.empty
	var caller = self

	def receive = {
		case x: Int => {
			totalCount = x
			caller = sender
		}
		case pair: Tuple2[Int,Matrix] => {
			seqOfMatrices = seqOfMatrices :+ pair
			if(seqOfMatrices.size == totalCount){
				val sortedPairs = seqOfMatrices.sortBy(thisPair => thisPair._1)
				val sortedMatrices = for(pair <- sortedPairs) yield pair._2
				val result = sortedMatrices.reduce { (left,right) => left * right }				
				caller ! result
			}
		}
		case _ => log.error("Aggregator received garbage")
	}
}
