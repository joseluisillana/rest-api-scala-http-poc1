package com.datiobd.mike.models

/**
  * Created by joseluisillanaruiz on 15/3/16.
  */
case class KafkaInputMessage(topicName: String, messageValue: String){
  require(!topicName.isEmpty, "topicName.empty")
  require(!messageValue.isEmpty, "messageValue.empty")
}
