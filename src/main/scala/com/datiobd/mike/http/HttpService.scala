package com.datiobd.mike.http

import akka.http.scaladsl.server.Directives._
import com.datiobd.mike.http.routes._
import com.datiobd.mike.utils.CorsSupport

trait HttpService extends UsersServiceRoute with AuthServiceRoute with CorsSupport {

  val routes =
    pathPrefix("v1") {
      corsHandler {
        usersRoute ~
          authRoute
      }
    }

}
