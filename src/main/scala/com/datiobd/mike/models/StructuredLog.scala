package com.datiobd.mike.models

/**
  * Created by joseluisillanaruiz on 15/3/16.
  */
case class StructuredLog(messageValue: String){
  require(!messageValue.isEmpty, "messageValue.empty")
}