enablePlugins(JavaServerAppPackaging)

name := "mike-reactive-api"
organization := "com.datiobd.mike"
version := "0.1"

scalaVersion := "2.11.7"
//scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  //"Akka Snapshot Repository" at "http://repo.akka.io/snapshots/",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Maven Central" at "http://repo1.maven.org/maven2",
  "Nexus Repository Manager" at "http://ec2-52-48-62-134.eu-west-1.compute.amazonaws.com:8081/nexus/")

libraryDependencies ++= {
  val akkaVersion = "2.4.2"
  val scalaTestVersion = "2.2.5"
  val typesafeConfigVersion = "1.3.0"
  val sprayJsonVersion = "1.3.2"
  val kafkaClientVersion = "0.9.0.1"
  val akkaStreamVersion = "2.0-M1"
  val scalaMockVersion = "3.2.2"
  val scalazScalaTestV = "0.2.3"
  val slickVersion = "3.1.0"
  val Json4sVersion     = "3.2.11"

  Seq(
    "com.typesafe"      %  "config"                               % typesafeConfigVersion,
    "com.typesafe.akka" %% "akka-actor"                           % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j"                           % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core"                       % akkaVersion, //A complete, mostly low-level, server- and client-side implementation of HTTP (incl. WebSockets)
    "com.typesafe.akka" %% "akka-cluster"                         % akkaVersion, //A complete, mostly low-level, server- and client-side implementation of HTTP (incl. WebSockets)
    "com.typesafe.akka" %% "akka-cluster-tools"                   % akkaVersion,
    "com.typesafe.akka" %% "akka-http-testkit"                    % akkaVersion, //A test harness and set of utilities for verifying server-side service implementations
    "com.typesafe.akka" %% "akka-http-spray-json-experimental"    % akkaVersion, //Predefined glue-code for (de)serializing custom types from/to JSON with spray-json
    "io.spray"          %% "spray-json"                           % sprayJsonVersion,
    "org.apache.kafka"  %% "kafka"                                % kafkaClientVersion
      excludeAll(ExclusionRule(organization = "com.sun.jdmk",name="jmxtools"),
        ExclusionRule(organization = "com.sun.jmx",name="jmxri"),
        ExclusionRule(organization = "javax.jms",name="jms")),
    "com.typesafe.akka"  %% "akka-stream-experimental"             % akkaStreamVersion,
    "com.typesafe.akka"  %% "akka-http-core-experimental"          % akkaStreamVersion,
    //"com.typesafe.akka"  %% "akka-http-spray-json-experimental"    % akkaStreamVersion,
    "org.mindrot"        %  "jbcrypt"                              % "0.3m",
    "org.flywaydb"       %  "flyway-core"                          % "3.2.1",
    "com.typesafe.slick" %% "slick"                                % slickVersion,
    //"org.scalatest"      %% "scalatest"                            % scalaTestVersion % "test",
    "org.scalamock"      %% "scalamock-scalatest-support"          % scalaMockVersion % "test",
    "com.typesafe.akka"  %% "akka-http-testkit-experimental"       % akkaStreamVersion      % "test",
    "org.json4s"        %% "json4s-native"   % Json4sVersion,
    "org.json4s"        %% "json4s-ext"      % Json4sVersion,
    "de.heikoseeberger" %% "akka-http-json4s" % "1.4.2"
  )
}

assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith(".idea")          => MergeStrategy.discard
  case m if m.toLowerCase.endsWith("manifest.mf")          => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*")      => MergeStrategy.discard
  case m if m.toLowerCase.startsWith("meta-inf/services/") => MergeStrategy.filterDistinctLines
  case "reference.conf"                                    => MergeStrategy.concat
  case _                                                   => MergeStrategy.first
}

artifact in (Compile, assembly) := {
  val art = (artifact in (Compile, assembly)).value
  art.copy(`classifier` = Some("assembly"))
}

addArtifact(artifact in (Compile, assembly), assembly)

publishMavenStyle := true

publishTo := {
  val nexus = "http://ec2-52-48-62-134.eu-west-1.compute.amazonaws.com:8081/nexus/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "content/repositories/releases")
}

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

// Assembly settings
mainClass in Global := Some("com.datiobd.mike.Main")

jarName in assembly := "mike-ingestion-reactive-api-poc-1.jar"

lazy val root = project.in(file(".")).configs(IntegrationTest)
Defaults.itSettings

Revolver.settings
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

dockerExposedPorts := Seq(9000)

dockerEntrypoint := Seq("bin/%s" format executableScriptName.value, "-Dconfig.resource=docker.conf")

parallelExecution in Test := false


