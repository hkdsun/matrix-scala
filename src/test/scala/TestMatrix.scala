package main.scala

import org.scalatest._


class TestMatrices extends FlatSpec with Matchers {
	trait TestMatrices {
		val mat1 = Matrix.empty addCol (1 to 5) addRow Seq(10) addCol (20 to 25) addCol (31 to 36) addCol (15 to 20) addRow Seq(1, 1, 1, 1)
		val mat32 = Matrix.empty addCol Seq(1,7,5) addCol Seq(10,6,8)
		val mat27 = Matrix.empty addRow Seq(5,8,1,4,8,4,4) addRow Seq(4,1,2,8,1,5,7)
		val identity3 = Matrix.empty addRow Seq(1,0,0) addRow Seq(0,1,0) addRow Seq(0,0,1)
		val identity2 = Matrix.empty addRow Seq(1,0) addRow Seq(0,1)
		val allzero3 = Matrix.empty addRow Seq(0,0,0) addRow Seq(0,0,0) addRow Seq(0,0,0)
		val allzero32 = Matrix.empty addRow Seq(0,0) addRow Seq(0,0) addRow Seq(0,0)
		val row4matrix = Matrix.empty addRow Seq(1,1,0,0)
		val col4matrix = Matrix.empty addCol Seq(1,2,3,4)
	}
	
	"getting columns" should "be sane" in {
    new TestMatrices {
			val x = Matrix.empty addCol (1 to 5) addRow Seq(10) addCol (20 to 25) addCol (31 to 36) addCol (15 to 20) addRow Seq(1, 1, 1, 1)
			mat1.getCol(0) should be (Vector(1,2,3,4,5,10,1))
			mat1.getCol(1) should be (Vector(20,21,22,23,24,25,1))
			mat1.getCol(2) should be (Vector(31,32,33,34,35,36,1))
			mat1.getCol(3) should be (Vector(15,16,17,18,19,20,1))
    }
  }

	"multiplication" should "work for base cases" in {
		new TestMatrices {
			(identity3 * mat32) should be (mat32)
			(mat32 * identity2) should be (mat32)
			(allzero3 * mat32) should be (allzero32)
			(row4matrix * col4matrix) should be (Matrix.empty addCol Seq(3))
			(col4matrix * row4matrix) should be (Matrix.empty addRow Seq(1,1,0,0) addRow Seq(2,2,0,0) addRow Seq(3,3,0,0) addRow Seq(4,4,0,0))
		}		
	}

	it should "work for complicated case" in {
		new TestMatrices {
			val res = Matrix.empty addRow Seq(45,18,21,84,18,54,74) addRow Seq(59,62,19,76,62,58,70) addRow Seq(57,48,21,84,48,60,76)
			(mat32 * mat27) should be (res)
		}		
	}
		


	it should "throw IllegalArgumentException when col and row don't match" in {
		new TestMatrices {
			a [IllegalArgumentException] should be thrownBy (mat27 * mat32)
		}
	}

	it should "throw IllegalArgumentException when any of the matrices is empty" in {
		new TestMatrices {
			a [IllegalArgumentException] should be thrownBy (mat27 * Matrix.empty)
			a [IllegalArgumentException] should be thrownBy (Matrix.empty * mat32)
			a [IllegalArgumentException] should be thrownBy (Matrix.empty * Matrix.empty)
		}
	}


}
