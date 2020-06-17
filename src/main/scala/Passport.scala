package main.scala

case class _id(`$oid`: String
              )

case class Taggings(predicate: String,
                    value: String
                   )

case class Passport(_id: _id,
                    passportId: Option[String],
                    language: Option[String],
                    locator: Option[String],
                    home: Option[String],
                    genre: Option[String],
                    taggings: List[Taggings]
                   )