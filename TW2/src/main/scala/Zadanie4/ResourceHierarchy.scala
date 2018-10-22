package Zadanie4

class PhilosopherHierarchy(private val id: Int,
                           private val forks: List[Fork],
                           private val philosophersNo: Int)
  extends Philosopher(id, forks, philosophersNo) {

  override def run(): Unit = {
    while (true) {
      think()
      val firstFork = forks(firstForkIndex).get()
      val secondFork = forks(secondForkIndex).get()
      eat()
      secondFork.put()
      firstFork.put()
    }
  }
}

class ResourceHierarchy {
  def test(philosophersNo: Int = 5): Unit = {
    val forks = (0 until philosophersNo).map(e => new Fork(e)).toList
    val philosophers = (0 until philosophersNo).map(e => new PhilosopherHierarchy(e, forks, philosophersNo))

    val philosophersThreads = philosophers.map(e => new Thread(e))

    philosophersThreads.foreach(e => e.start())

    Thread.sleep(10000)

    philosophersThreads.foreach(e => e.stop())
  }
}