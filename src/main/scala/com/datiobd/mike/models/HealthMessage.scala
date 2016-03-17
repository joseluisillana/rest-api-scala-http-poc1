package com.datiobd.mike.models

import com.datiobd.mike.tools.JsonSupport

/**
  * Created by joseluisillanaruiz on 15/3/16.
  */
case class HealthMessage(message: String, date: String) extends JsonSupport{
  require(!message.isEmpty, "message.empty")
  require(!date.isEmpty, "date.empty")
}