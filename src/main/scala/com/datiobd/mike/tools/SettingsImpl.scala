package com.datiobd.mike.tools

import akka.actor._
import com.typesafe.config.Config

/**
  * Created by joseluisillanaruiz on 10/3/16.
  */
class SettingsImpl(config: Config) extends Extension {
  val serverPort: Int = config.getInt("application.port")
}

object Settings extends ExtensionId[SettingsImpl] with ExtensionIdProvider {

  override def lookup = Settings

  override def createExtension(system: ExtendedActorSystem) =
    new SettingsImpl(system.settings.config)

  /**
    * Java API: retrieve the Settings extension for the given system.
    */
  override def get(system: ActorSystem): SettingsImpl = super.get(system)
}
