package main.scala

import scala.io.Source
import org.json4s._
import org.json4s.jackson.JsonMethods._

object OfflineCompletion {
  implicit val formats = DefaultFormats
  var predicatesMap = Map[String, Int]()

  def main(args: Array[String]): Unit = {
    val source = Source.fromResource("snippet.json")
    val teamArg = args(0)
    val team = s"http://www.bbc.co.uk/ontologies/passport/home/$teamArg"

    var totalTeamPassports = 0

    for (line <- source.getLines) {
      val passport = parse(line).extract[Passport]
      passport.home match {
        case Some(`team`) =>
          totalTeamPassports += 1
          mapPredicates(passport.taggings)
        case _ =>
      }
    }
    for ((predicate, count) <- predicatesMap) {
      val predicateFormatted = "[^/]+$".r findFirstIn predicate
      println(predicateFormatted.get + ": " + f"${count.toDouble / totalTeamPassports * 100}%1.2f" + "%")
    }
  }

  def mapPredicates(taggings: List[Taggings]) = {
    val predicates = taggings.map(tagging => tagging.predicate)
    for (predicate <- predicates.distinct) {
      if (predicatesMap.contains(predicate)) {
        val count = predicatesMap.get(predicate)
        predicatesMap += (predicate -> (count.get + 1))
      } else {
        predicatesMap += (predicate -> 1)
      }
    }
  }
}