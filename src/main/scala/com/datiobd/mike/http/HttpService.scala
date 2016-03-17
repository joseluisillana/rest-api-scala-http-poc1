package com.datiobd.mike.http

import akka.actor.Actor
import com.datiobd.mike.http.routes._
import com.datiobd.mike.services.IngestionService
import com.datiobd.mike.utils.CorsSupport

import scala.concurrent.ExecutionContext

trait HttpService extends Resources {
  implicit def executionContext: ExecutionContext

  lazy val ingestionService = new IngestionService

  val routes =
    corsHandler {
      structuredLogRoute
    }
}

trait Resources extends IngestionServiceRoute with CorsSupport
