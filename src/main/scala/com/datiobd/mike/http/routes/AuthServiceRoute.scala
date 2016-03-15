package com.datiobd.mike.http.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.datiobd.mike.http.SecurityDirectives
import com.datiobd.mike.models.UserEntity
import com.datiobd.mike.services.AuthService
import spray.json._

trait AuthServiceRoute extends AuthService with BaseServiceRoute with SecurityDirectives {

  import StatusCodes._

  case class LoginPassword(login: String, password: String)

  implicit val loginPasswordFormat = jsonFormat2(LoginPassword)

  val authRoute = pathPrefix("auth") {
    path("signIn") {
      pathEndOrSingleSlash {
        post {
          entity(as[LoginPassword]) { loginPassword =>
            complete(signIn(loginPassword.login, loginPassword.password).map(_.toJson))
          }
        }
      }
    } ~
      path("signUp") {
        pathEndOrSingleSlash {
          post {
            entity(as[UserEntity]) { userEntity =>
              complete(Created -> signUp(userEntity).map(_.toJson))
            }
          }
        }
      }
  }

}
