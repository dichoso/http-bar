package com.example

//#quick-start-server
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.actor.{ ActorRef, ActorSystem, Props, Actor, Inbox }
import akka.pattern.ask
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext.Implicits.global

//#main-class
object QuickstartServer extends App with UserRoutes {

  // set up ActorSystem and other dependencies here
  //#main-class
  //#server-bootstrapping
  implicit val system: ActorSystem = ActorSystem("bar-des-acteurs")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  //#server-bootstrapping

  val avenir: ActorRef = system.actorOf(Props[Bar], "Avenir")
  var barman: Option[ActorRef] = None
  (avenir ? FindABarman).mapTo[ActorFound] foreach ((af) => {
    barman = Some(af.actor)
  })

  //#main-class
  // from the UserRoutes trait
  lazy val routes: Route = userRoutes
  //#main-class

  //#http-server
  Http().bindAndHandle(routes, "localhost", 8080)

  println(s"Server online at http://localhost:8080/")

  Await.result(system.whenTerminated, Duration.Inf)
  //#http-server
  //#main-class
}
//#main-class
//#quick-start-server
