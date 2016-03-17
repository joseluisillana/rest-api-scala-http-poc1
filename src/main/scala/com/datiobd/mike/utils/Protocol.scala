package com.datiobd.mike.utils

import akka.http.scaladsl.marshalling.{ToResponseMarshallable, ToResponseMarshaller}
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.{Directives, Route}
import com.datiobd.mike.models._
import com.datiobd.mike.tools.JsonSupport
//import spray.json.DefaultJsonProtocol

import scala.concurrent.{ExecutionContext, Future}

trait Protocol extends Directives with JsonSupport {
  /*implicit val responseFormat = jsonFormat2(ResponseMessage)
  implicit val kafkaInputMessageFormat = jsonFormat2(KafkaInputMessage)
  implicit val structuredLogFormat = jsonFormat1(StructuredLog)
  implicit val healthMessageFormat = jsonFormat2(HealthMessage)
*/

  implicit def executionContext: ExecutionContext

  def completeWithLocationHeader[T](resourceId: Future[Option[T]], ifDefinedStatus: Int, ifEmptyStatus: Int): Route =
    onSuccess(resourceId) {
      case Some(t) => completeWithLocationHeader(ifDefinedStatus, t)
      case None => complete(ifEmptyStatus, None)
    }

  def completeWithLocationHeader[T](status: Int, resourceId: T): Route =
    extractRequestContext { requestContext =>
      val request = requestContext.request
      val location = request.uri.copy(path = request.uri.path / resourceId.toString)
      respondWithHeader(Location(location)) {
        complete(status, None)
      }
    }

  def complete[T: ToResponseMarshaller](resource: Future[Option[T]]): Route =
    onSuccess(resource) {
      case Some(t) => complete(ToResponseMarshallable(t))
      case None => complete(404, None)
    }

  def complete(resource: Future[Unit]): Route = onSuccess(resource) { complete(204, None) }
}
