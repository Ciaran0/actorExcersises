import akka.actor.{Props, Actor}
import akka.event.Logging

import scala.util.Random

/**
 * Compute average of 1,000,000 numbers by distributing work
 */

class Average extends Actor{

  val numbersToBeComputed = Array.fill(1000000)(Random.nextInt())

  var answer =0

  val log = Logging(context.system, this)

  override def preStart(): Unit = {
    val calculator =  context.actorOf(Props[RangeCalcultor])
    val calculator2 = context.actorOf(Props[RangeCalcultor])
    log.info("size "+ numbersToBeComputed.size)
    log.info("element 100= "+numbersToBeComputed(100))
    calculator ! numbersToBeComputed.slice(0,500000)
    calculator2 ! numbersToBeComputed.slice(500000,1000000)
  }

  def receive = {
    case rangeCalculated: Int  => {
      answer += rangeCalculated
      log.info("calculation ="+answer)
    }
    case _ => log.error("Unexpected message")
  }
}

class RangeCalcultor extends Actor {

  val log = Logging(context.system, this)

  def receive = {
    case x: Array[Int]  => {
      log.info("received range. size = "+x.size)
      sender ! x.sum
    }
    case _ => log.error("unexpected message")
  }
}


