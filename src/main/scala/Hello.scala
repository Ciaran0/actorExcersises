import akka.actor.{Props, Actor}
import akka.event.Logging

/*

Hello World actor program!

 */
class Hello extends Actor {

  val log = Logging(context.system, this)
  def receive = {
    case "test" => {
      log.info("received test")
      //to do send message back to sender
      sender ! "Message back"
    }
    case _      => log.info("received something other than test")
  }

}

class Sender extends Actor {
  val log = Logging(context.system, this)

  override def preStart(): Unit = {
    val greeter =  context.actorOf(Props[Hello], "hello")
    greeter ! "test"
  }

  def receive = {
    case _ => log.info("received response")
  }
}
