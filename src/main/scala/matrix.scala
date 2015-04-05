package main.scala

final case class Matrix(rows: Vector[Vector[Int]]) {
	def addCol(col: Seq[Int]): Matrix = {
		require((!rows.isEmpty && rows.size == col.size) || rows.isEmpty, "Dimension of the new column is wrong.")
		val es = if (rows.isEmpty)
			(col map { _ ⇒ Vector.empty[Int] }).toVector
		else
			rows
		Matrix(
			es.zip(col).map {
				case (row, colValue) ⇒
					row :+ colValue
			}
		)
	}

	def addRow(row: Seq[Int]): Matrix = {
		require((!rows.isEmpty && rows.head.size == row.size) || rows.isEmpty, "Dimension of the new row is wrong.")
		Matrix(rows :+ row.toVector)
	}

	def pretty: String = {
		def maxLen(): Int = {
			val sizes = for {
				row ← rows
				col ← row
			} yield col.toString.size
			if (sizes.isEmpty)
				0
			else
				sizes.max
		}

		val max = maxLen()
		val s = rows.map { row ⇒
			val s = row map { i ⇒ s"%${max}d".format(i) }
			s.mkString("[ ", ", ", " ]")
		}
		s.mkString("\n")
	}

	def getCol (idx: Int): Vector[Int]= {
		for(i <- rows) yield i(idx)
	}
	
	def * (right: Matrix) : Matrix = {
		def multiply(acc: Matrix, rows: IndexedSeq[IndexedSeq[Int]], cols: IndexedSeq[IndexedSeq[Int]]): Matrix = {
			rows match {
				case Vector() => acc
				case row +: xs => {
					
					val flatRow = for{
						col <- cols
						i <- 0 until row.length
					} yield col(i)*row(i)
					
					val newRow = flatRow.grouped(cols.head.length).map(x => x.sum).toSeq
					
					multiply(acc.addRow(newRow),xs,cols)
				}
			}
		}
		
		require(!rows.isEmpty && !right.rows.isEmpty, "Matrices cannot be empty")
		require(right.rows.size == this.rows.head.size, "Number of rows in the right matrix doesn't match number of columns of the left matrix")
		
		val columns: IndexedSeq[IndexedSeq[Int]] = for(i <- 0 until right.rows.head.length) yield right.getCol(i)
		multiply(Matrix.empty, this.rows, columns)
	}
}

object Matrix {
	val empty = Matrix(Vector.empty)
}
