import akka.actor.Actor

/*


 */

class DirectoryTraverseWordCount extends Actor{

  /*
    This actor traverses the directory recursively.
    Creates a new actor for each file
   */
  override def preStart(): Unit = {

  }

  def getRecursiveListOfFiles(dir: File): Array[File] = {
    Files.newDirectoryStream(path).filter(_.endsWith(".txt")).map(_.toAbsolutePath)
    val these = dir.listFiles
    these ++ these.filter(_.isDirectory).flatMap(getRecursiveListOfFiles)
  }

  def receive = {

  }
}
