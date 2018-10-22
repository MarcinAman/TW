package Zadanie4

import java.util.concurrent.Semaphore

class PhilosopherWithWaiter(private val id: Int,
                            private val forks: List[Fork],
                            private val philosophersNo: Int,
                            private val waiter: Waiter)
  extends Philosopher(id, forks, philosophersNo) {

  override def run(): Unit = {
    while (true) {
      think()
      waiter.get()
      val firstFork = forks(firstForkIndex).get()
      val secondFork = forks(secondForkIndex).get()
      eat()
      firstFork.put()
      secondFork.put()
      waiter.put()
    }
  }
}

class Waiter(private val philosophesNo: Int = 5) {
  private val semaphore = new Semaphore(philosophesNo - 1)

  def get(): Unit = {
    semaphore.acquire()
  }

  def put(): Unit = {
    semaphore.release()
  }
}

class WaiterSolution {
  def test(philosophersNo: Int = 5): Unit = {
    val forks = (0 until philosophersNo).map(e => new Fork(e)).toList
    val philosophers = (0 until philosophersNo).map(e => new PhilosopherHierarchy(e, forks, philosophersNo))

    val philosophersThreads = philosophers.map(e => new Thread(e))

    philosophersThreads.foreach(e => e.start())

    Thread.sleep(10000)

    philosophersThreads.foreach(e => e.stop())
  }
}


