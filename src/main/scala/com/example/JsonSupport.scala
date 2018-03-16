package com.example

import com.example._

//#json-support
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val userJsonFormat = jsonFormat2(Boisson)
  implicit val paramsJsonFormat = jsonFormat2(PostParameter)
  //implicit val usersJsonFormat = jsonFormat1(Users)

  //implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)
}
//#json-support
