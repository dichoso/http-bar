package com.example

import akka.actor.{ ActorRef, ActorSystem }
import akka.event.Logging
import scala.concurrent.duration._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.delete
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.MethodDirectives.post
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.directives.PathDirectives.path

import scala.concurrent.Future
import com.example._
import akka.pattern.ask
import akka.util.Timeout

import spray.json._
import DefaultJsonProtocol._

import scala.concurrent.ExecutionContext.Implicits.global

case class PostParameter(val command: Int, val address: String)

case class Boisson(val name: String, val price: Int)

//#user-routes-class
trait UserRoutes extends JsonSupport {

  val menu = List(Boisson("thé", 1), Boisson("café", 2), Boisson("tisane", 1))
  //#user-routes-class

  // we leave these abstract, since they will be provided by the App
  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[UserRoutes])

  // other dependencies that UserRoutes use
  def barman: Option[ActorRef]

  // Required by the `ask` (?) method below
  implicit lazy val timeout = Timeout(5.seconds) // usually we'd obtain the timeout from the system's configuration

  //#all-routes
  //#users-get-post
  //#users-get-delete   
  lazy val userRoutes: Route =
    concat(
      pathPrefix("menu") {
        concat(
          //#users-get-delete
          pathEnd {
            get {
              complete(menu.toJson)
            }
          }
        )
      },
      pathPrefix("command") {
        pathEnd {
          post {
            entity(as[PostParameter]) { params =>
              barman match {
                case Some(barman) => {
                  val f = (barman ? Order(menu(params.command).toString)).mapTo[Conso]
                  onSuccess(f) { (conso) =>
                    complete("Done: " + conso.conso)
                  }
                }
              }
            }

            /*
            entity(as[User]) { user =>
              val userCreated: Future[ActionPerformed] =
                (userRegistryActor ? CreateUser(user)).mapTo[ActionPerformed]
              onSuccess(userCreated) { performed =>
                log.info("Created user [{}]: {}", user.name, performed.description)
                complete((StatusCodes.Created, performed))
              }
            }*/
          }
        }
      }
    )

  //#all-routes
}
