package com.datiobd.mike.utils

import com.typesafe.config.ConfigFactory

trait Config {
  private val config = ConfigFactory.load()
  private val httpConfig = config.getConfig("http")
  private val kafkaConfig = config.getConfig("kafka")

  val httpInterface = httpConfig.getString("host")
  val httpPort = httpConfig.getInt("port")

  val kafkaBrokerList= kafkaConfig.getString("kafka.broker.list")

  // KAFKA PRODUCER TUNNING
  val kafkaRequestRequiredAcks = kafkaConfig.getInt("kafka.request.required.acks")
  val kafkaProducerType = kafkaConfig.getString("kafka.producer.type")
  val kafkaBatchNumMessages = kafkaConfig.getInt("kafka.batch.num.messages")
  val kafkaQueueBufferingMaxMS = kafkaConfig.getInt("kafka.queue.buffering.max.ms")

}
