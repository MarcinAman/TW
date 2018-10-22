package Zadanie4

class Commons {

}

import Zadanie1.BinarySemaphore

class Philosopher(private val id: Int, private val forks: List[Fork], private val philosophersNo: Int) extends Runnable{
  val firstForkIndex: Int = id
  val secondForkIndex: Int = (id+1) % philosophersNo

  private var eatingCounter = 0
  private var thinkingCounter = 0

  def think(): Unit = {
    thinkingCounter += 1
    println("philosopher no " + id + " is thinking for " + thinkingCounter + "th time")
    Thread.sleep(5)
    println("philosopher no " + id + " stopped thinking for " + thinkingCounter + "th time")
  }

  def eat(): Unit = {
    eatingCounter += 1
    println("philosopher no " + id + " is eating for " + eatingCounter + "th time")
    Thread.sleep(5)
    println("philosopher no " + id + " stopped eating for " + eatingCounter + "th time")
  }

  override def run(): Unit = ???
}

class Fork(private val id: Int){
  private val semaphore = new BinarySemaphore()

  def get(): Fork = {
    semaphore.take()
    this
  }

  def put(): Fork = {
    semaphore.give()
    this
  }
}
