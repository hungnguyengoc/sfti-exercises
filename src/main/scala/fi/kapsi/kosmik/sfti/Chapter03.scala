package fi.kapsi.kosmik.sfti

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object Chapter03 {

  /**
    * Write a loop that swaps adjacent elements of an array of integers.
    */
  object Ex02 {
    def swap(arr: Array[Int]): Array[Int] = {
      for (i <- 0 until arr.length - 1 by 2) {
        val first = arr(i)
        arr(i) = arr(i + 1)
        arr(i + 1) = first
      }
      arr
    }
  }

  /**
    * Repeat the preceding assignment, but produce a new array with the swapped values. Use for / yield.
    */
  object Ex03 {
    def swap(arr: Array[Int]): Array[Int] = {
      (for (i <- arr.indices)
        yield if (i % 2 == 1) arr(i - 1) else if (i == arr.length - 1) arr(i) else arr(i + 1))
        .toArray
    }
  }

  /**
    * Given an array of integers, produce a new array that contains all positive
    * values of the original array, in their original order, followed by all values that
    * are zero or negative, in their original order.
    */
  object Ex04 {
    def partition(arr: Array[Int]): Array[Int] = {
      val ordered = ArrayBuffer[Int]()
      ordered.appendAll(arr.filter(_ >= 0))
      ordered.appendAll(arr.filter(_ < 0))
      ordered.toArray
    }
  }

  /**
    * How do you compute the average of an Array[Double]?
    */
  object Ex05 {
    def avg(arr: Array[Double]): Double = arr.sum / arr.length
  }

  /**
    * How do you rearrange the elements of an Array[Int] so that they appear in
    * reverse sorted order?
    * How do you do the same with an ArrayBuffer[Int]?
    */
  object Ex06 {
    def reverseSort(arr: Array[Int]): Array[Int] = arr.sortWith(_ > _)

    def reverseSort(arr: ArrayBuffer[Int]): ArrayBuffer[Int] = arr.sortWith(_ > _)
  }

  /**
    * Write a code snippet that produces all values from an array with duplicates
    * removed.
    */
  object Ex07 {
    def uniq(arr: Array[Int]): Array[Int] = arr.distinct
  }

  /**
    * Suppose you are given an array buffer of integers and want to remove all but
    * the first negative number... by collecting
    * positions of the negative elements, dropping the first element, reversing the
    * sequence, and calling a.remove(i) for each index.
    */
  object Ex08 {
    def removeAllButFirstNegative(arr: ArrayBuffer[Int]): ArrayBuffer[Int] = {
      val indicesToDrop = arr.zipWithIndex
        .filter({ case (e, _) => e < 0 })
        .map({ case (_, i) => i })
        .tail

      for (iDrop <- indicesToDrop.reverse)
        arr.remove(iDrop)

      arr
    }
  }

  /**
    * Improve the solution of the preceding exercise by collecting the positions
    * that should be moved and their target positions. Make those moves and
    * truncate the buffer. Don’t copy any elements before the first unwanted
    * element.
    */
  object Ex09 {
    def removeAllButFirstNegative(arr: ArrayBuffer[Int]): ArrayBuffer[Int] = {
      val moves = ArrayBuffer[(Int, Int)]()

      var currTargetIndex: Int = arr.zipWithIndex
        .filter({ case (e, _) => e < 0 })
        .map({ case (_, i) => i })
        .take(2)
        .last

      for ((e, i) <- arr.zipWithIndex.takeRight(arr.length - currTargetIndex)) {
        if (e > 0) {
          moves.append((i, currTargetIndex))
          currTargetIndex += 1
        }
      }

      for ((index, targetIndex) <- moves)
        arr(targetIndex) = arr(index)

      arr.take(moves.last._2 + 1)
    }
  }

  /**
    * Make a collection of all time zones returned by java.util.TimeZone.getAvailableIDs
    * that are in America. Strip off the "America/" prefix and sort the result.
    * NOTE: Only returns first 6 so as to simplify testing
    */
  object Ex10 {
    def americanZones(): Array[String] =
      java.util.TimeZone.getAvailableIDs
        .filter(_.startsWith("America/"))
        .map(_.stripPrefix("America/"))
        .sorted
        .take(6)
  }

  /**
    * Import java.awt.datatransfer._ and ... return value as a Scala buffer.
    */
  object Ex11 {
    def imageFlavors(): mutable.Buffer[String] = {
      import java.awt.datatransfer._

      import scala.collection.JavaConverters._

      val flavors = SystemFlavorMap.getDefaultFlavorMap.asInstanceOf[SystemFlavorMap]
      flavors.getNativesForFlavor(DataFlavor.imageFlavor).asScala
    }
  }

}