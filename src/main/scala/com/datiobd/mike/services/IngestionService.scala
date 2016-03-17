package com.datiobd.mike.services

import akka.actor.{Actor, PoisonPill}
import com.datiobd.mike.models.{KafkaInputMessage, ResponseMessage}
import com.datiobd.mike.utils.Protocol
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by joseluisillanaruiz on 15/3/16.
  */

//object IngestionService extends IngestionService

class IngestionService(implicit val executionContext: ExecutionContext) {

  /*def putMessage(kafkaInputMessage: KafkaInputMessage): Future[Option[ResponseMessage]] = Future{
    sendMessageToKafka(kafkaInputMessage) match {
      case Some(result) =>
        //self ! killYourself
        Some(ResponseMessage("ok","ok"))
      case None =>
        None

    }
  }*/

  def putMessage(kafkaInputMessage: KafkaInputMessage): Future[Option[String]] = Future {
    sendMessageToKafka(kafkaInputMessage) match {
      case Some(q) => Some("1") // Conflict! id is already taken
      case None =>
        None
    }
  }


  //private def killYourself = {self ! PoisonPill}

  private def sendMessageToKafka(kafkaInputMessage: KafkaInputMessage): Option[Any] = {
    val result:Any = true
    val max = 100L
    var i = 0
    while (i < max) {
      System.out.println(s"MIKE PET NUM ${i} of ${max} data ingested: ${kafkaInputMessage.topicName} and the messege is ${kafkaInputMessage.messageValue}")
      i = i + 1
    }
    Some(result)
  }
}