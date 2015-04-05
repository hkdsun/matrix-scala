package main.scala

import akka.util.Timeout
import akka.actor._
import akka.actor.ActorSystem
import akka.actor.Props
import akka.event.Logging
import akka.pattern.ask
import scala.concurrent.{ Future, Promise, Await }
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

import scala.io.Source



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

	val groups = seqOfMatrices.grouped(4)

	val identity = Matrix.empty addRow Seq(1,0,0) addRow Seq(0,1,0) addRow Seq(0,0,1)

	val futures = groups map { matrices => Future { matrices.reduceLeft { (left,right) => left * right } } }
	
	val almostDone = Future.sequence(futures)
	
	val done = almostDone.map { matrices => matrices.reduceLeft { (left,right) => left * right } }

	done onComplete {
		case Success(result) => println(result.pretty)
		case Failure(error) => println(error)
	}
	
	Await.result(done, 5000 millis)
}
