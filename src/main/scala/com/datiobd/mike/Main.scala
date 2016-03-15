package com.datiobd.mike

import akka.actor.ActorSystem
import akka.event.{ Logging, LoggingAdapter }
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import com.datiobd.mike.http.HttpService
import com.datiobd.mike.utils.{ Migration, Config }

import scala.concurrent.ExecutionContext

/**
  * Created by joseluisillanaruiz on 10/3/16.
  */
object Main extends App with Config with HttpService with Migration {
  private implicit val system = ActorSystem()

  override protected implicit val executor: ExecutionContext = system.dispatcher
  override protected val log: LoggingAdapter = Logging(system, getClass)
  override protected implicit val materializer: ActorMaterializer = ActorMaterializer()

  migrate()

  Http().bindAndHandle(routes, httpInterface, httpPort)
}