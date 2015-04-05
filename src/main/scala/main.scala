package main.scala

import java.util.concurrent.TimeUnit

import akka.util.Timeout
import akka.actor._
import akka.actor.ActorSystem
import akka.actor.Props
import akka.event.Logging
import akka.pattern.ask
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

import main.scala.Calculator._
import scala.io.Source



class Dispatcher extends Actor{
	val calculator = context.actorOf(Calculator.props, name = "calculatorActor")
	val log = Logging(context.system, this)
	context.watch(calculator)
	def receive = {
		case seqOfMatrices: Seq[Matrix] => {
			val groups = seqOfMatrices.grouped(4)
			for(group <- groups) calculator ! group
		}
		case result: Matrix => println(result.pretty)
		case Terminated(calculator) => context.system.shutdown
		case _ => log.error("Dispatcher received data in error")
	}
}



object Main extends App{
	def fetchMatrices(i: Int): Seq[Matrix] = {
		def getTestDir = new java.io.File( "./test_cases/" ).getCanonicalPath
		var fileName = ""
		if(i == 1) fileName = getTestDir + "/matrices.dat"
		//future test cases for i=2,3,...,n
		require(!fileName.isEmpty, "bad test case")
		val fileLines = io.Source.fromFile(fileName).getLines.toList
		val stringList = fileLines.map(x => x.split(';').map(y => y.split(',')))
		val intList = stringList.map(_.map(_.map(_.toInt)))

		for(matrix <- intList) yield matrix.foldLeft(Matrix.empty)((result,nextRow) => result addRow nextRow)
	}

	val seqOfMatrices = fetchMatrices(1)

/*************************************
**      FUTURE IMPLEMENTATION				**
*************************************/

/**
	val groups = seqOfMatrices.grouped(4)

	val futures = groups map { matrices => Future { matrices.reduceLeft { (left,right) => left * right } } }
	
	val almostDone = Future.sequence(futures)
	
	val done = almostDone.map { matrices => matrices.reduceLeft { (left,right) => left * right } }

	done onComplete {
		case Success(result) => println(result.pretty)
		case Failure(error) => println(error)
	}**/


//TODO: Actor implementation
	val system = ActorSystem("MatrixSystem")
	val dispatcher = system.actorOf(Props[Dispatcher], name = "dispatcherActor")
	dispatcher ! seqOfMatrices
	system.awaitTermination
}
