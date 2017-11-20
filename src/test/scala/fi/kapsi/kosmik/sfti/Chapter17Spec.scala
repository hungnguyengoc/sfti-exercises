package fi.kapsi.kosmik.sfti

import java.time.Duration
import java.util.concurrent.Executors

import fi.kapsi.kosmik.sfti.{Chapter17 => chapter}
import org.scalatest.{AsyncFunSpec, Matchers}

import scala.concurrent.{ExecutionContext, Future}

class Chapter17Spec extends AsyncFunSpec with Matchers with ExerciseSupport {
  /**
    * Use this execution context for code that needs to run concurrently. The implicit execution context provided
    * by AsyncFunSpec confines execution to a single thread and should not be used if you, for example, define
    * asynchronous functions in test code and expect the production code to run these concurrently.
    */
  private val concurrentExecutionContext = ExecutionContext.fromExecutor(Executors.newCachedThreadPool())

  describe("Exercise 01") {
    import chapter.Ex01._

    it("should run futureDemonstration") {
      val futureSum = futureDemonstration()
      futureSum map { sum => assert(sum == 42) }
    }
  }

  describe("Exercise 02") {
    import chapter.Ex02._

    it("should do g(f(t)) when g and f are functions that produce one Future each") {
      def f = (t: Double) => Future[Int] {
        Thread.sleep(18)
        t.floor.toInt
      }

      def g = (u: Int) => Future[String] {
        Thread.sleep(11)
        "#" * u
      }

      doInOrder(f, g)(3.2) map { v => assert(v == "###") }
    }
  }

  describe("Exercise 03") {
    import chapter.Ex03._

    it("should do fn(...(f1(f0(t)))) when f is a function that produces a Future") {
      // t * 2 - 3 / 3 + 1
      val fs = Seq[Int => Future[Int]](
        (t: Int) => Future {
          Thread.sleep(2000)
          t * 2
        },
        (t: Int) => Future {
          Thread.sleep(500)
          t - 3
        },
        (t: Int) => Future {
          Thread.sleep(100)
          t / 3
        },
        (t: Int) => Future {
          Thread.sleep(1000)
          t + 1
        }
      )

      // Use assertDurationLess to verify doInOrder is non-blocking.
      val eventualInt = assertDurationLess(Duration.ofMillis(100)) {
        doInOrder(fs)(6)
      }
      eventualInt map { v => assert(v == 4) }
    }

    describe("Exercise 04") {
      import chapter.Ex04._

      it("should yield (f(t), g(t)) by running these asynchronous functions concurrently") {
        val f = (t: Int) => Future[Int] {
          Thread.sleep(2000)
          ("1" * t).toInt
        }(concurrentExecutionContext)

        val g = (t: Int) => Future[String] {
          Thread.sleep(500)
          "x" * t
        }(concurrentExecutionContext)

        val eventualResult = assertDurationLess(Duration.ofMillis(100)) {
          doTogether(f, g)(5)
        }
        eventualResult map {
          case (aRes, bRes) =>
            assert(aRes == 11111)
            assert(bRes == "xxxxx")
        }
      }
    }
  }
}