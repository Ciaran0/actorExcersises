import java.io.File

import akka.actor.{Props, ActorLogging, Actor}
import akka.event.Logging

import scala.util.matching.Regex

/*


 */

class DirectoryTraverseWordCount extends Actor with ActorLogging{

  /*
    This actor traverses the directory recursively.
    Creates a new actor for each file
   */


  import java.io.File
  override def preStart(): Unit = {
    val  file = new File("filesToBeRead")
    val files = recursiveListFiles(file,new Regex(".*.txt"))
    for( file <- files) {
      val counter = context.actorOf(Props[CountNumWordsInFile], "CountNumWordsInFile"+Math.random())
      counter ! file
    }
  }

  import java.io.File
  import scala.util.matching.Regex
  def recursiveListFiles(f: File, r: Regex): Array[File] = {
    val these = f.listFiles
    val good = these.filter(f => r.findFirstIn(f.getName).isDefined)
    good ++ these.filter(_.isDirectory).flatMap(recursiveListFiles(_,r))
  }

  def receive = {
    case _ => log.info("blah")
  }
}

class CountNumWordsInFile extends Actor with ActorLogging {


  def receive = {
    case file:File =>{
      log.info("received file. Starting to count words in it")
    }
    case _ => log.error("unexpected Message")
  }
}
