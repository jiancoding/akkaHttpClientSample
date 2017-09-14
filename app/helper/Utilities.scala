package helper

trait Utilities {

  def createUrl(baseUrl: String, paraMap: Map[String, String]): String = {
    val subString = paraMap.filter(_._2.nonEmpty).map {
      case (key, value) => s"$key=$value"
    }.mkString("&")
    s"$baseUrl$subString"
  }

}
