package com.datiobd.mike.http.routes

import java.text.SimpleDateFormat
import java.util.Calendar

import akka.actor.{Actor, ActorLogging, PoisonPill, Props}
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings}
import akka.http.javadsl.server.HttpApp
import akka.http.scaladsl.model.StatusCodes
import com.datiobd.mike.actors.Mensajero
import com.datiobd.mike.models.{HealthMessage, KafkaInputMessage, StructuredLog}
import com.datiobd.mike.services.IngestionService
import com.datiobd.mike.utils.Protocol

trait IngestionServiceRoute extends BaseServiceRoute with Protocol {
  import StatusCodes._
  val ingestionService: IngestionService

  val structuredLogRoute = pathPrefix("mikeApi") {
    pathEndOrSingleSlash {
      get {
        complete(BadRequest)
      }
    } ~
      pathPrefix("_healthcheck") {
        pathEndOrSingleSlash {
            get {
              complete(OK -> HealthMessage("MIKE API STATUS", getCurrentHour))
            }
        }
      } ~
        pathPrefix("log") {
          pathPrefix("structured" / Segment) {
            (topicName: String) => {
              pathEnd {
                  post {
                    entity(as[StructuredLog]) { structuredLog =>

                     completeWithLocationHeader(
                        resourceId = ingestionService.putMessage(KafkaInputMessage(topicName,structuredLog.messageValue)),
                        ifDefinedStatus = 204, ifEmptyStatus = 500)
                      //val kafkaWriter = createKafkaWriter(Sender(topicName, structuredLog.messageValue))
                      //kafkaWriter ! SendToKafka
                      //complete(NoContent)// -> ResponseMessage(NoContent.toString(), "MIKE - Received ").toJson)
                    }
                  }
              }
            }
          }
        }
    }

  private def getCurrentHour: String = {
    val today = Calendar.getInstance().getTime()
    val dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss")
    return dateFormat.format(today)
  }

}
