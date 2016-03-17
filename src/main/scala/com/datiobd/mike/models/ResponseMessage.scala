package com.datiobd.mike.models

/**
  * Created by joseluisillanaruiz on 15/3/16.
  */
case class ResponseMessage(statusCode: String, statusMessage: String){
  require(!statusCode.isEmpty, "username.empty")
  require(!statusMessage.isEmpty, "password.empty")
}
