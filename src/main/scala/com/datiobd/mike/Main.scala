package com.datiobd.mike

import akka.actor._
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.datiobd.mike.actors.Mensajero
import com.datiobd.mike.http.HttpService
import com.datiobd.mike.utils.Config
import akka.cluster.singleton.{ClusterSingletonManagerSettings, ClusterSingletonManager}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
  * Created by joseluisillanaruiz on 10/3/16.
  */
object Main extends App with Config with HttpService {
  implicit val system = ActorSystem("mike-reactive-ingestion-api")

  implicit val executionContext = system.dispatcher
  override protected val log: LoggingAdapter = Logging(system, getClass)
  override protected implicit val materializer: ActorMaterializer = ActorMaterializer()

  /*val tickCounter = {
    val singletonProps = ClusterSingletonManager.props(
      singletonProps = Props[Mensajero],
      terminationMessage = PoisonPill,
      settings = ClusterSingletonManagerSettings(system)
    )
    system.actorOf(singletonProps, "tick-counter-singleton")
  }*/

  implicit val timeout = Timeout(10 seconds)

  Http().bindAndHandle(handler = routes, interface = httpInterface, port = httpPort) map { binding =>
    println(s"REST interface bound to ${binding.localAddress}") } recover { case ex =>
    println(s"REST interface could not bind to $httpInterface:$httpPort", ex.getMessage)
  }
}