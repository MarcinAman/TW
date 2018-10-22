import Zadanie1.{BinarySemaphore, BinarySemaphoreTest}
import Zadanie2.BinarySemaphoreWithoutLoop
import Zadanie4.{ResourceHierarchy, WaiterSolution}

object Main {
  private def zadanie1(): Unit ={
    val binarySemaphoreTest = new BinarySemaphoreTest
    binarySemaphoreTest.test(100,2000, new BinarySemaphore)

    binarySemaphoreTest.test(100,2000,new BinarySemaphoreWithoutLoop)
  }

  private def zadanie4WithHierarchy(): Unit = {
    val resourceHierarchy = new ResourceHierarchy
    resourceHierarchy.test()
  }

  private def zadanie4WithWaiter(): Unit = {
    val waiterSolution = new WaiterSolution
    waiterSolution.test()
  }

  def main(args: Array[String]): Unit = {
    this.zadanie4WithHierarchy()
  }
}
