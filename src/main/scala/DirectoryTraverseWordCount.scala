import java.io.File

import akka.actor.{Actor, ActorLogging, Props}

import scala.io.Source
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
      //      val counter = context.actorOf(Props[CountNumWordsInFile])

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
    case _ => log.info("unexpected message")
  }
}

object CountNumWordsInFile {
  val counter = System.actorOf(Props[CountTotal], "CountTotal")
}

class CountNumWordsInFile extends Actor with ActorLogging {



  def receive = {
    case file:File =>{
      log.info("received file. Starting to count words in it")
      counter ! countWordsInFile(file)
    }
    case _ => log.error("unexpected Message")
  }

  def countWordsInFile(file: File) : Int = {
    val src = Source.fromFile(file)

      (for {
        line <- src.getLines
      } yield {
        val words = line.split("\\s+")
        words.size
      }).sum

  }
}

class CountTotal extends Actor with ActorLogging {

  var total =0

  def receive = {
    case x:Int => {
      total = total + x
      log.info("count = "+ total)
    }
    case _ => log.error("unexpected message")
  }

}
