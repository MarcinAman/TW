package Zadanie3

import java.util.concurrent.Semaphore

import Zadanie1.BinarySemaphore

/**
  * V -> release
  *
  * P -> acquire
  */

// http://www.cs.umd.edu/~shankar/412-Notes/10x-countingSemUsingBinarySem.pdf
class CountingSemaphore(private val permits: Int)
  extends Semaphore(permits) {

  private var currentPermits = permits
  private val gate = new BinarySemaphore(locked = permits == 0)
  private val mutex = new BinarySemaphore()

  def give(): Unit = synchronized {
    gate.take()
    mutex.take()
    currentPermits -= 1
    if(currentPermits > 0){
      gate.give()
    }
    mutex.give()
  }

  def take(): Unit = synchronized {
    mutex.take()
    currentPermits += 1
    if(currentPermits == 1){
      gate.give()
    }
    mutex.give()
  }
}
