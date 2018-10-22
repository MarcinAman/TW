package Zadanie2

import Zadanie1.BinarySemaphore

class BinarySemaphoreWithoutLoop extends BinarySemaphore {
  override def take(): Unit = synchronized {
    if(this.locked){
      wait()
    }
    this.locked = true
  }
}
