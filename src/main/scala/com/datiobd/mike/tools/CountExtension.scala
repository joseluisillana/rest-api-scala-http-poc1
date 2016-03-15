package com.datiobd.mike.tools

import java.util.concurrent.atomic.AtomicLong

import akka.actor._

/**
  * Created by joseluisillanaruiz on 10/3/16.
  */
class CountExtensionImpl extends Extension {
  //Since this Extension is a shared instance
  // per ActorSystem we need to be threadsafe
  private val counter = new AtomicLong(0)

  //This is the operation this Extension provides
  def increment() = counter.incrementAndGet()
}

object CountExtension
  extends ExtensionId[CountExtensionImpl]
    with ExtensionIdProvider {
  //The lookup method is required by ExtensionIdProvider,
  // so we return ourselves here, this allows us
  // to configure our extension to be loaded when
  // the ActorSystem starts up
  override def lookup = CountExtension

  //This method will be called by Akka
  // to instantiate our Extension
  override def createExtension(system: ExtendedActorSystem) = new CountExtensionImpl

  /**
    * Java API: retrieve the Count extension for the given system.
    */
  override def get(system: ActorSystem): CountExtensionImpl = super.get(system)
}