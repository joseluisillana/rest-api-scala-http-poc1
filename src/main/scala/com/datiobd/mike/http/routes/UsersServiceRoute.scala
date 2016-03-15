package com.datiobd.mike.http.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.IntNumber
import com.datiobd.mike.http.SecurityDirectives
import com.datiobd.mike.models.{ UserEntityUpdate, UserEntity }
import com.datiobd.mike.services.UsersService

import spray.json._

trait UsersServiceRoute extends UsersService with BaseServiceRoute with SecurityDirectives {

  import StatusCodes._

  implicit val usersUpdateFormat = jsonFormat2(UserEntityUpdate)

  val usersRoute = pathPrefix("users") {
    pathEndOrSingleSlash {
      get {
        complete(getUsers().map(_.toJson))
      }
    } ~
      pathPrefix("me") {
        pathEndOrSingleSlash {
          authenticate { loggedUser =>
            get {
              complete(loggedUser)
            } ~
              post {
                entity(as[UserEntityUpdate]) { userUpdate =>
                  complete(updateUser(loggedUser.id.get, userUpdate).map(_.toJson))
                }
              }
          }
        }
      } ~
      pathPrefix(IntNumber) { id =>
        pathEndOrSingleSlash {
          get {
            complete(getUserById(id).map(_.toJson))
          } ~
            post {
              entity(as[UserEntityUpdate]) { userUpdate =>
                complete(updateUser(id, userUpdate).map(_.toJson))
              }
            } ~
            delete {
              onSuccess(deleteUser(id)) { ignored =>
                complete(NoContent)
              }
            }
        }
      }
  }

}
