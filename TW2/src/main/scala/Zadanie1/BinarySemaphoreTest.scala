package Zadanie1

import java.util.concurrent.CyclicBarrier

class BinarySemaphoreTest {
  def test(threadNo: Int, iterations: Int, semaphore: BinarySemaphore): Unit = {
    var acc = 0
    val gate = new CyclicBarrier(threadNo) // just to synchronize the start of every thread

    val threadRoutine = () => {
      gate.await()

      for(_ <- 1 to iterations){
        semaphore.take()
        acc += 1
        semaphore.give()
      }
    }

    val threads = (1 to threadNo).map(_ => new Thread(() => {
      threadRoutine()
    }))

    threads.foreach(e => e.start())

    threads.foreach(e => e.join())

    displayResult(acc,threadNo,iterations)
  }

  private def displayResult(acc: Int, threadsNo: Int, iterations: Int) : Unit = {
    println("Number of threads: " + threadsNo)
    println("result: " + acc)
    println("Should be: " + threadsNo * iterations)
  }
}
