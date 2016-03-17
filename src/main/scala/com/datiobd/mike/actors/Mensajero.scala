package com.datiobd.mike.actors

import akka.actor.{PoisonPill, ActorLogging, Actor}
import akka.http.scaladsl.server.RequestContext
import com.datiobd.mike.models.KafkaInputMessage

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import akka.http.scaladsl.model.StatusCodes.{NoContent, InternalServerError}
/**
  * Created by joseluisillanaruiz on 17/3/16.
  */
case object SenderResponseOK

case object SenderResponseKO

case object SendToKafka

case object KillActor

class Mensajero extends Actor with ActorLogging{

  case class Tick(n: Int)

  override def preStart = self ! Tick(0)

  def receive = {
    case Tick(t) =>
      println(s"${self.path} - Tick $t")
      context.system.scheduler.scheduleOnce(1 second, self, Tick(t+1))
  }

}

class Responder(requestContext: RequestContext) extends Actor with ActorLogging {


  def receive = {

    case SenderResponseOK =>
      requestContext.complete(NoContent)
      killYourself
    case SenderResponseKO =>
      requestContext.complete(InternalServerError)
      killYourself
  }

  private def killYourself = self ! PoisonPill

}

class KafkaWriter(kafkaInputMessage: KafkaInputMessage) extends Actor with ActorLogging {


  def receive = {

    case SendToKafka =>
      // Do something
      sendMessageToKafka(kafkaInputMessage) match {
        case true => self ! KillActor
        case _ => self ! KillActor
      }
    case KillActor =>
      killYourself
  }

  private def killYourself = self ! PoisonPill

  private def sendMessageToKafka(kafkaInputMessage: KafkaInputMessage): Boolean = {
    val result = true
    val max = 10000L
    var i = 0
    while (i < max) {
      System.out.println(s"MIKE PET NUM ${i} of ${max} data ingested: ${kafkaInputMessage.topicName} and the messege is ${kafkaInputMessage.messageValue}")
      i = i + 1
    }
    result
  }
}