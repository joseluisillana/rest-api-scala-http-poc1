package com.datiobd.mike.utils

import com.datiobd.mike.models._
import spray.json.DefaultJsonProtocol

trait Protocol extends DefaultJsonProtocol {
  implicit val usersFormat = jsonFormat3(UserEntity)
  implicit val tokenFormat = jsonFormat3(TokenEntity)
}
