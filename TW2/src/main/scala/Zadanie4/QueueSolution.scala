package Zadanie4

import Zadanie1.BinarySemaphore

import scala.collection.mutable

//https://www.cs.utexas.edu/users/misra/scannedPdf.dir/DrinkingPhil.pdf
class PhilosopherQueueSolution(private val id: Int,
                               private val forks: List[Fork],
                               private val philosophersNo: Int,
                               private val queue: mutable.Queue[Int],
                               private val queueSemaphore: BinarySemaphore
                              ) extends Philosopher(id, forks, philosophersNo) {

  override def run(): Unit = {
    while (true) {
      think()
      queueSemaphore.take()
      if (queue.front == id) {
        eat()
        queue.dequeue()
        queue.enqueue(id)
      }
      queueSemaphore.give()
    }
  }
}

class QueueSolution {
  def test(philosophersNo: Int = 5): Unit = {
    val indexQueue: mutable.Queue[Int] = new mutable.Queue()
    val semaphore = new BinarySemaphore()

    (0 until philosophersNo).foreach(e => indexQueue.enqueue(e))

    val forks = (0 until philosophersNo).map(e => new Fork(e)).toList
    val philosophers = (0 until philosophersNo).map(e => new PhilosopherQueueSolution(e, forks, philosophersNo, indexQueue, semaphore))

    val philosophersThreads = philosophers.map(e => new Thread(e))

    philosophersThreads.foreach(e => e.start())

    Thread.sleep(10000)

    philosophersThreads.foreach(e => e.stop())
  }
}

/*
    0 para:
    0 i 2
    1 para:
    1 i 3
    2 para:
    2 i 4
    Na zasadzie master/slave?
     */
