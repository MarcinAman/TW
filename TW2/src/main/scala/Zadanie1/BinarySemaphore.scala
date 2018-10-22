package Zadanie1

import java.util.concurrent.Semaphore

class BinarySemaphore(var locked: Boolean = false,
                      private val permits: Int = 1)
  extends Semaphore(permits) {

  def give(): Unit = synchronized {
    while(this.locked){
      this.locked = false
      notify()
    }
  }

  def take(): Unit = synchronized {
    while(this.locked){
      wait()
    }
    this.locked = true
  }
}
